var g_tagify;
var g_guide;
/**
 * 스케쥴 등록 팝업 초기화
 * @param data 초기 데이터
 */
function initializePopup (data)
{
    // 스케쥴 팝업 select 용 데이터 가공
    var selectArr = new Array();
    for (var idx = 0; idx < data.length; idx++)
    {
        selectArr.push({"id" : data[idx].id , "text" : data[idx].name, "color" : data[idx].color});
    }
    function formatTypes (type) {
        if (!type.id) {
            return type.text;
        }
        var $type = $(
            "<span><i class=\"c-icon cil-circle\" style=\"background-color: " + type.color + ";\"></i>&nbsp;&nbsp;" + type.text + "</span>"
        );
        return $type;
    }
    // 스케쥴 팝업 select 구성
    $("#scheduleType-select").select2({
        placeholder: "스케쥴 종류를 선택해주세요.",
        allowClear: true,
        width : "100%",
        data : selectArr,
        templateResult: formatTypes
    });
    // 스케쥴 팝업 select 구성
    $("#customer-select").select2({
        placeholder: "고객을 선택해주세요.",
        allowClear: true,
        width : "100%",
        minimumInputLength : 1,
        ajax: {
            url: SERVER_URL + "/customer-api/customer",
            method: "GET",
            data : function (params) {return { keyword: params.term , page : params.page || 0};},
            processResults: function (data, params) {
                params.page = data.number || 0;
                var returnArr = [];
                data.content.forEach(customer => returnArr.push({"id" : customer.id, "text" : customer.name + "(" + customer.typeName + " / " + customer.telNo + ")"}));
                return {
                    results: returnArr,
                    pagination: {
                        more: (params.page * 10) < data.totalElements
                    }
                };
            }
        }
    });
    // 스케쥴 팝업 tagify 구성
    var tagInput = document.querySelector("#tags-input");
    $.ajax({
        url : SERVER_URL + "/schedule-api/tag",
        method : "GET",
    }).done((data, textStatus, jqXHR) => {
        var tagData = []
        data.forEach(tag => tagData.push({"value" : tag.title}));
        g_tagify = new Tagify(tagInput, {
            pattern: /^.{0,20}$/,
            whitelist : tagData,
            dropdown : {
                position: "input",
                enabled: 1, // suggest tags after a single character input
                fuzzySearch: false
            }, // map tags
            backspace : false
        });
        g_tagify.DOM.input.classList.add('form-control');
        g_tagify.DOM.scope.parentNode.insertBefore(g_tagify.DOM.input, g_tagify.DOM.scope);
    });
    // 팝업이 닫히면 가이드라인 제거
    $('#scheduleModal').on('hidden.bs.modal', function (e) {
        if (g_guide != undefined)
        {
            g_guide.clearGuideElement();
        }
        // 버튼 숨김
        $("#save-schedule-button").hide();
        $("#update-schedule-button").hide();
    });

    // 버튼 숨김
    $("#save-schedule-button").hide();
    $("#update-schedule-button").hide();
    $("#delete-schedule-button").hide();
}

/**
 * 새로운 스케쥴을 생성하는 팝업을 오픈한다.
 * @param event
 */
function openNewPopup (event)
{
    var start = event.start ? new Date(event.start.getTime()) : new Date();
    var end = event.end ? new Date(event.end.getTime()) : moment().add(1, 'hours').toDate();

    // 팝업 구성
    $("#modal-title").text("스케쥴 등록하기");
    $("#name-input").val("");
    $("#scheduleType-select").val(null).trigger('change');
    $("#customer-select").val(null).trigger('change');
    $("input:checkbox[id='isAllDay-check']").prop("checked", false);
    $("#stt-date-input").val(moment(start).format("YYYY-MM-DD\THH:mm"));
    $("#end-date-input").val(moment(end).format("YYYY-MM-DD\THH:mm"));
    $("#bigo-input").val("");

    // 태그 제거
    g_tagify.removeAllTags();
    
    // 저장 버튼 보이게
    $("#save-schedule-button").show();

    // 팝업 오픈
    $('#scheduleModal').modal({
        show: true
    });
    g_guide = event.guide;
}

/**
 * 저장 버튼 클릭 이벤트 핸들러
 */
$("#save-schedule-button").on("click", function (e){
    var data = $("#modal-schedule-form").serializeObject();
    var startDate = data.startDate;
    var tagData = [];
    if (data.tags != null && data.tags != "")
    {
        JSON.parse(data.tags).forEach(tag => tagData.push({"title":tag.value}));
    }
    data.tags = tagData;
    $.ajax({
        url : SERVER_URL + "/schedule-api/schedules/",
        method : "POST",
        dataType : "json",
        data : JSON.stringify(data),
        contentType : "application/json; charset=utf-8"
    }).always(function(){
        $('#scheduleModal').modal('hide');
        cal.setDate(new Date(startDate));
        cal.changeView(cal.getViewName(), true);
        setRenderRangeText();
        setSchedules();
    });
});

/**
 * 스케쥴 상세 팝업을 오픈한다.
 * @param event
 */
function openEditPopup (event)
{
    // 팝업 구성
    $("#modal-title").text("스케쥴 상세 팝업");
    $("#schedule-id-input").val(event.schedule.id);
    $("#name-input").val(event.schedule.title);
    $("#scheduleType-select").val(event.schedule.calendarId).trigger('change');
    $("input:checkbox[id='isAllDay-check']").prop("checked", event.schedule.isAllDay);
    $("#stt-date-input").val(moment(event.schedule.start.toDate()).format("YYYY-MM-DD\THH:mm:ss"));
    $("#end-date-input").val(moment(event.schedule.end.toDate()).format("YYYY-MM-DD\THH:mm:ss"));
    $("#bigo-input").val(event.schedule.body);

    // 고객
    if (event.schedule.raw.customerId != undefined && event.schedule.raw.customerId != "")
    {
        $.ajax({
            url : SERVER_URL + "/customer-api/customer/" + event.schedule.raw.customerId,
            method : "GET",
        }).done((data, textStatus, jqXHR) => {
            var newOption = new Option(data.name + "(" + data.typeName + " / " + data.telNo + ")", data.id, false, true);
            $("#customer-select").append(newOption).trigger('change');
        });
    }

    // 태그 추가
    var tagData = []
    event.schedule.raw.tags.forEach(tag => tagData.push({"value" : tag.title}));
    g_tagify.removeAllTags();
    g_tagify.addTags(tagData);

    // 수정 버튼 보이게
    $("#update-schedule-button").show();
    $("#delete-schedule-button").show();

    // 팝업 오픈
    $('#scheduleModal').modal({
        show: true
    });
}

/**
 * 수정 버튼 클릭 이벤트 핸들러
 */
$("#update-schedule-button").on("click", function (e){
    var scheduleId = $("#schedule-id-input").val();
    var data = $("#modal-schedule-form").serializeObject();
    var startDate = data.startDate;
    var tagData = [];
    if (data.tags != null && data.tags != "")
    {
        JSON.parse(data.tags).forEach(tag => tagData.push({"title":tag.value}));
    }
    data.tags = tagData;
    $.ajax({
        url : SERVER_URL + "/schedule-api/schedules/" + scheduleId,
        method : "PUT",
        dataType : "json",
        data : JSON.stringify(data),
        contentType : "application/json; charset=utf-8"
    }).always(function(){
        $('#scheduleModal').modal('hide');
        cal.setDate(new Date(startDate));
        cal.changeView(cal.getViewName(), true);
        setRenderRangeText();
        setSchedules();
    });
});

/**
 * 삭제 버튼 클릭 이벤트 핸들러
 */
$("#delete-schedule-button").on("click", function (e){
    var rtn = confirm("이 스케쥴을 삭제하시겠습니까?");
    if(rtn)
    {
        var scheduleId = $("#schedule-id-input").val();
        var data = $("#modal-schedule-form").serializeObject();
        var startDate = data.startDate;
        $.ajax({
            url : SERVER_URL + "/schedule-api/schedules/" + scheduleId,
            method : "DELETE",
        }).always(function(){
            $('#scheduleModal').modal('hide');
            cal.setDate(new Date(startDate));
            cal.changeView(cal.getViewName(), true);
            setRenderRangeText();
            setSchedules();
        });
    }
});


function setRenderRangeText() {
    var renderRange = document.getElementById('renderRange');
    var options = cal.getOptions();
    var viewName = cal.getViewName();

    var html = [];
    if (viewName === 'day') {
        html.push(currentCalendarDate('YYYY.MM.DD'));
    } else if (viewName === 'month' &&
        (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
        html.push(currentCalendarDate('YYYY.MM'));
    } else {
        html.push(moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD'));
        html.push(' ~ ');
        html.push(moment(cal.getDateRangeEnd().getTime()).format(' MM.DD'));
    }
    renderRange.innerHTML = html.join('');
}

function currentCalendarDate(format) {
    var currentDate = moment([cal.getDate().getFullYear(), cal.getDate().getMonth(), cal.getDate().getDate()]);

    return currentDate.format(format);
}

/**
 * 스케쥴을 세팅한다.
 */
function setSchedules()
{
    cal.clear();

    var startDateTime = cal.getDateRangeStart();
    var endDateTime = cal.getDateRangeEnd();
    var startDate = moment(startDateTime.toDate()).format('YYYY-MM-DD');
    var endDate = moment(endDateTime.toDate()).format('YYYY-MM-DD');
    var calendarType;

    switch (cal.getViewName())
    {
        case "day"   : calendarType = "DAILY";
        case "week"  : calendarType = "WEEKLY";
        case "month" : calendarType = "MONTHLY";
    }

    $.ajax({
        url : SERVER_URL + "/schedule-api/schedules",
        type : "GET",
        data : {"startDate" : startDate, "endDate" : endDate , "calendarType" : calendarType}
    }).done((data, textStatus, jqXHR) => {
        // 스케쥴 초기화
        ScheduleList = [];
        // 스케쥴 생성
        for (var idx = 0; idx < data.length; idx++)
        {
            var rawData = data[idx];
            var schedule = new ScheduleInfo();

            schedule.id = rawData.id;
            schedule.calendarId = rawData.scheduleType;

            schedule.title = rawData.name;
            schedule.body = rawData.bigo;
            schedule.isReadOnly = false;

            schedule.isAllday = rawData.isAllDay;
            if (schedule.isAllday)
            {
                schedule.category = 'allday';
            }
            else
            {
                schedule.category = 'time';
            }
            schedule.start = rawData.startDate;
            schedule.end = rawData.endDate;

            schedule.isPrivate = false;
            schedule.location = "";
            schedule.attendees = [];
            schedule.recurrenceRule = "";
            schedule.state = "";
            schedule.color = "black";
            schedule.bgColor = rawData.scheduleColor;
            schedule.dragBgColor = rawData.scheduleColor;
            schedule.borderColor = rawData.scheduleColor;

            if (schedule.category === 'milestone') {
                schedule.color = schedule.bgColor;
                schedule.bgColor = 'transparent';
                schedule.dragBgColor = 'transparent';
                schedule.borderColor = 'transparent';
            }

            schedule.raw.customerId = rawData.customerId;
            schedule.raw.userId = rawData.userId;
            schedule.raw.payed = rawData.payed;
            schedule.raw.tags = rawData.tags;

            ScheduleList.push(schedule);
        }
        cal.createSchedules(ScheduleList);
        refreshScheduleVisibility();
    });
}


function refreshScheduleVisibility() {
    var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));

    ScheduleTypes.forEach(function(calendar) {
        cal.toggleSchedules(calendar.id, !calendar.checked, false);
    });

    cal.render(true);

    calendarElements.forEach(function(input) {
        var span = input.nextElementSibling;
        span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
    });
}