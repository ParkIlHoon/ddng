var g_tagify;
var g_guide;
/**
 * 스케쥴 등록 팝업 초기화
 * @param data 초기 데이터
 */
function initializePopup ()
{
    // 스케쥴 팝업 select 구성
    $("#scheduleType-select").select2({
        placeholder: "스케쥴 종류를 선택해주세요.",
        allowClear: true,
        width : "100%",
        data : g_popupSelect,
        templateResult: function (type){
            if (!type.id) {
                return type.text;
            }
            var $type = $(
                "<span><i class=\"c-icon cil-circle\" style=\"background-color: " + type.color + ";\"></i>&nbsp;&nbsp;" + type.text + "</span>"
            );
            return $type;
        }
    });
    // 스케쥴 팝업 select 구성
    $("#customer-select").select2({
        placeholder: "고객을 선택해주세요.",
        allowClear: true,
        width : "100%",
        minimumInputLength : 1,
        ajax: {
            url: "/schedule/customers",
            method: "GET",
            data : function (params) {return { keyword: params.term , page : params.page || 0 , size : 10};},
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
    g_tagify = new Tagify(tagInput, {
        pattern: /^.{0,20}$/,
        whitelist : g_popupWhitelist,
        dropdown : {
            position: "input",
            enabled: 1, // suggest tags after a single character input
            fuzzySearch: false
        }, // map tags
        backspace : false
    });
    g_tagify.DOM.input.classList.add('form-control');
    g_tagify.DOM.scope.parentNode.insertBefore(g_tagify.DOM.input, g_tagify.DOM.scope);

    // 팝업이 닫히면 가이드라인 제거
    $('#scheduleModal').on('hidden.bs.modal', function (e) {
        if (g_guide != undefined)
        {
            g_guide.clearGuideElement();
        }
        // 버튼 숨김
        $("#save-schedule-button").hide();
        $("#update-schedule-button").hide();
        $("#delete-schedule-button").hide();
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

    var form = document.getElementById('modal-schedule-form');
    var button = document.getElementById('save-schedule-button');
    var originalHtml = button.innerHTML;

    button.disabled = true;
    button.innerHTML = "<span class=\"spinner-border spinner-border-sm\" role=\"status\" aria-hidden=\"true\"></span>\n  저장중...";

    if (form.checkValidity() === false)
    {
        e.preventDefault();
        e.stopPropagation();
        button.disabled = false;
        button.innerHTML = originalHtml;
    }
    form.classList.add('was-validated');
    if (form.checkValidity() === false)
    {
        return;
    }

    var data = $("#modal-schedule-form").serializeObject();
    var startDate = data.startDate;
    var tagData = [];
    if (data.tags != null && data.tags != "")
    {
        JSON.parse(data.tags).forEach(tag => tagData.push({"title":tag.value}));
    }
    data.tags = tagData;
    data.allDay = document.getElementById("isAllDay-check").checked;
    $.ajax({
        url : "/schedule/add",
        method : "POST",
        dataType : "json",
        data : JSON.stringify(data),
        contentType : "application/json; charset=utf-8"
    }).always(function(){
        form.classList.remove('was-validated');
        button.disabled = false;
        button.innerHTML = originalHtml;
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
    document.getElementById("isAllDay-check").checked = event.schedule.isAllDay;
    $("#stt-date-input").val(moment(event.schedule.start.toDate()).format("YYYY-MM-DD\THH:mm"));
    $("#end-date-input").val(moment(event.schedule.end.toDate()).format("YYYY-MM-DD\THH:mm"));
    $("#bigo-input").val(event.schedule.body);

    // 고객
    if (event.schedule.raw.customerId != undefined && event.schedule.raw.customerId != "")
    {
        $.ajax({
            url : "/schedule/customers/" + event.schedule.raw.customerId,
            method : "GET",
        }).done((data, textStatus, jqXHR) => {
            var newOption = new Option(data.name + "(" + data.typeName + " / " + data.telNo + ")", data.id, false, true);
            $("#customer-select").append(newOption).trigger('change');
        });
    }

    // 태그 추가
    var tagData = []
    if (event.schedule.raw.tags != null)
    {
        event.schedule.raw.tags.forEach(tag => tagData.push({"value" : tag.title}));
    }
    g_tagify.removeAllTags();
    g_tagify.addTags(tagData);

    if (event.schedule.raw.payed)
    {
        $("#name-input").attr("disabled",true);
        $("#scheduleType-select").prop("disabled", true);
        $("#customer-select").prop("disabled", true);
        $("#isAllDay-check").attr("disabled",true);
        $("#stt-date-input").attr("disabled",true);
        $("#end-date-input").attr("disabled",true);
        $("#bigo-input").attr("disabled",true);
        $("#tags-input").parent().attr("disabled",true);
        $("#update-schedule-button").hide();
        $("#delete-schedule-button").hide();
    }
    else
    {
        $("#name-input").removeAttr("disabled");
        $("#scheduleType-select").prop("disabled", false);
        $("#customer-select").prop("disabled", false);
        $("#isAllDay-check").removeAttr("disabled");
        $("#stt-date-input").removeAttr("disabled");
        $("#end-date-input").removeAttr("disabled");
        $("#bigo-input").removeAttr("disabled");
        $("#tags-input").parent().removeAttr("disabled");
        // 수정 버튼 보이게
        $("#update-schedule-button").show();
        $("#delete-schedule-button").show();
    }

    // 팝업 오픈
    $('#scheduleModal').modal({
        show: true
    });
}

/**
 * 수정 버튼 클릭 이벤트 핸들러
 */
$("#update-schedule-button").on("click", function (e)
{

    var form = document.getElementById('modal-schedule-form');
    var button = document.getElementById('save-schedule-button');
    var originalHtml = button.innerHTML;

    button.disabled = true;
    button.innerHTML = "<span class=\"spinner-border spinner-border-sm\" role=\"status\" aria-hidden=\"true\"></span>\n  저장중...";

    if (form.checkValidity() === false)
    {
        e.preventDefault();
        e.stopPropagation();
        button.disabled = false;
        button.innerHTML = originalHtml;
    }
    form.classList.add('was-validated');
    if (form.checkValidity() === false)
    {
        return;
    }

    var scheduleId = $("#schedule-id-input").val();
    var data = $("#modal-schedule-form").serializeObject();
    var startDate = data.startDate;
    var tagData = [];
    if (data.tags != null && data.tags != "")
    {
        JSON.parse(data.tags).forEach(tag => tagData.push({"title":tag.value}));
    }
    data.tags = tagData;
    data.allDay = document.getElementById("isAllDay-check").checked;
    $.ajax({
        url : "/schedule/update/" + scheduleId,
        method : "POST",
        dataType : "json",
        data : JSON.stringify(data),
        contentType : "application/json; charset=utf-8"
    }).always(function(){
        form.classList.remove('was-validated');
        button.disabled = false;
        button.innerHTML = originalHtml;
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
            url : "/schedule/remove/" + scheduleId,
            method : "POST",
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
        url : "/schedule/search",
        type : "GET",
        data : {"startDate" : startDate, "endDate" : endDate}
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

            schedule.allDay = rawData.allDay;
            if (schedule.allDay)
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

$("#isAllDay-check").change(function(e){
    var checked = $(this).is(":checked");

    if (checked)
    {
        var date = $("#stt-date-input").val();
        var start = moment(date).startOf('day').format("YYYY-MM-DD\THH:mm");
        var end = moment(date).endOf('day').format("YYYY-MM-DD\THH:mm");

        $("#stt-date-input").val(start);
        $("#end-date-input").val(end);
    }
})