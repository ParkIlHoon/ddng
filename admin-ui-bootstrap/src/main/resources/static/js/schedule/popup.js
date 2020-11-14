/**
 * 스케쥴 등록 팝업 초기화
 * @param data 초기 데이터
 */
function initializePopup (data)
{
    var g_tagify;

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
            url: "http://1hoon.iptime.org:8366/customer-api/customer",
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
    g_tagify = new Tagify(tagInput, {
        pattern: /^.{0,20}$/,
        whitelist : [],
        dropdown : {
            position: "input",
            enabled: 1, // suggest tags after a single character input
            fuzzySearch: false
        }, // map tags
        backspace : false
    });
}

/**
 * 새로운 스케쥴을 생성하는 팝업을 오픈한다.
 * @param event
 */
function openPopup (event)
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

    // 팝업 오픈
    $('#scheduleModal').modal({
        show: true
    });
    // 팝업이 닫히면 가이드라인 제거
    $('#scheduleModal').on('hidden.bs.modal', function (e) {
        event.guide.clearGuideElement();
    });
}