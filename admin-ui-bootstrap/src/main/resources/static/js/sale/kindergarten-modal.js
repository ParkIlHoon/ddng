/**
 * 유치원 모달 컴포넌트 초기화 함수
 */
function initKindergartenModal ()
{
    $("#add-kindergarten-button").on("click", function(e){
        var scheduleId = $("#kindergarten-id-input").val();
        var selectedItems = $("#kindergarten-modal-form").serializeObject();

        var keys = Object.keys(selectedItems);
        var itemId = [];

        for (var idx = 0; idx < keys.length; idx++)
        {
            var key = keys[idx];

            if (Number(selectedItems[key]) > 0)
            {
                for (var idx2 = 0; idx2 < Number(selectedItems[key]); idx2++)
                {
                    itemId.push(key);
                }
            }
        }

        // 장바구니에 추가
        $.ajax({
            url: "/sale/cart",
            type: "POST",
            dataType : "JSON",
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify({
                scheduleId : scheduleId,
                itemIds : itemId,
                couponId : null
            })
        }).always(function (jqXHR) {
            $('#kindergarten-modal').modal('hide');
            $("#item-list").replaceWith(jqXHR.responseText);
        });
    });
}

/**
 * 유치원 모달 오픈 함수
 * @param data
 */
function openKindergartenModal (data)
{
    // 데이터 초기화
    resetKindergartenModal();

    // 스케쥴 데이터 조회
    $.ajax({
        url: SERVER_URL + "/schedule-api/schedules/" + data.id,
        type: "GET",
        success : function (result){
            // 모달 element에 값 세팅
            setKindergartenData(result);
            // 모달 오픈
            $('#kindergarten-modal').modal({
                show: true
            });
        }
    });
}

/**
 * 유치원 모달 데이터 초기화 함수
 */
function resetKindergartenModal ()
{
    $("#kindergarten-id-input").val("");
    $("#kindergarten-schedule-name").text("");
    $("#kindergarten-schedule-type").empty();
    $("#kindergarten-schedule-duration").text("");
    $("#kindergarten-schedule-customer-profileImg").attr("src", "");
    $("#kindergarten-schedule-customer").text("");
    $("#kindergarten-schedule-tags").empty();
    $("#kindergarten-schedule-bigo").val("");
}

/**
 * 모달에 유치원 데이터 세팅
 * @param data
 */
function setKindergartenData (data)
{
    $("#kindergarten-id-input").val(data.id);
    $("#kindergarten-schedule-name").text(data.name);
    $("#kindergarten-schedule-type").append("<i class=\"c-icon cil-circle\" id=\"hotel-schedule-type-icon\" style=\"background-color:" + data.scheduleColor + "\"></i> " + data.scheduleTypeName);

    var duration = moment(data.startDate).format("YYYY-MM-DD HH:mm") + " ~ " + moment(data.endDate).format("YYYY-MM-DD HH:mm");
    var days = moment(data.endDate).diff(moment(data.startDate), 'days') + 1;
    $("#kindergarten-schedule-duration").text(duration + " (" + days + "일)");

    $.ajax({
        url: SERVER_URL + "/customer-api/customer/" + data.customerId,
        type: "GET",
        success : function (result){
            $("#kindergarten-schedule-customer-profileImg").attr("src", result.profileImg);
            $("#kindergarten-schedule-customer").text(result.name + " (" + result.typeName + " / " + result.telNo + ")");
        }
    });

    for(var idx = 0; idx < data.tags.length; idx++)
    {
        $("#kindergarten-schedule-tags").append("<span class=\"badge badge-secondary\">" + data.tags[idx].title + "</span>");
    }

    $("#kindergarten-schedule-bigo").val(data.bigo);
}