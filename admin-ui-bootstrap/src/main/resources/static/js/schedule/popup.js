var g_tagify;
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

    // 팝업 오픈
    $('#scheduleModal').modal({
        show: true
    });
    // 팝업이 닫히면 가이드라인 제거
    $('#scheduleModal').on('hidden.bs.modal', function (e) {
        event.guide.clearGuideElement();
    });
}
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
    }).done((data, textStatus, jqXHR) => {
        //TODO 캘린더 새로고침
        $('#scheduleModal').modal({
            show: false
        });
    });
});

function openEditPopup (event)
{
    // 팝업 구성
    $("#modal-title").text("스케쥴 상세 팝업");
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

    // 팝업 오픈
    $('#scheduleModal').modal({
        show: true
    });
}