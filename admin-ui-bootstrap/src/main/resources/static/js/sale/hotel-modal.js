/**
 * 호텔 모달 컴포넌트 초기화 함수
 */
function initHotelModal ()
{
    $("#add-hotel-button").on("click", function(e){
        var scheduleId = $("#hotel-id-input").val();
        /** @type Object[] */
        var selectedBeauties = $("#beauty-select").select2('data');
        var selectedCoupon = $("#beauty-modal-form").serializeObject()["beauty-coupons"];

        var beauties = [];
        selectedBeauties.forEach(b => beauties.push(Number(b.id)));

        // 장바구니에 추가
        $.ajax({
            url: "/sale/cart",
            type: "POST",
            dataType : "JSON",
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify({
                scheduleId : scheduleId,
                itemIds : beauties,
                couponId : selectedCoupon
            })
        }).always(function (jqXHR) {
            $('#beauty-modal').modal('hide');
            $("#item-list").replaceWith(jqXHR.responseText);
        });
    });
}

/**
 * 호텔 모달 오픈 함수
 * @param data
 */
function openHotelModal (data)
{
    // 데이터 초기화
    resetHotelModal();

    // 스케쥴 데이터 조회
    $.ajax({
        url: SERVER_URL + "/schedule-api/schedules/" + data.id,
        type: "GET",
        success : function (result){
            // 모달 element에 값 세팅
            setHotelData(result);
            // 모달 오픈
            $('#hotel-modal').modal({
                show: true
            });
        }
    });
}

/**
 * 호텔 모달 데이터 초기화 함수
 */
function resetHotelModal ()
{
    $("#hotel-id-input").val("");
    $("#hotel-schedule-name").text("");
    $("#hotel-schedule-type").empty();
    $("#hotel-schedule-duration").text("");
    $("#hotel-schedule-customer-profileImg").attr("src", "");
    $("#hotel-schedule-customer").text("");
    $("#hotel-schedule-tags").empty();
    $("#hotel-schedule-bigo").val("");
}

/**
 * 모달에 호텔 데이터 세팅
 * @param data
 */
function setHotelData (data)
{
    $("#hotel-id-input").val(data.id);
    $("#hotel-schedule-name").text(data.name);
    $("#hotel-schedule-type").append("<i class=\"c-icon cil-circle\" id=\"hotel-schedule-type-icon\" style=\"background-color:" + data.scheduleColor + "\"></i> " + data.scheduleTypeName);

    var duration = moment(data.startDate).format("YYYY-MM-DD HH:mm") + " ~ " + moment(data.endDate).format("YYYY-MM-DD HH:mm");
    var days = moment(data.endDate).diff(moment(data.startDate), 'days') + 1;
    $("#hotel-schedule-duration").text(duration + " (" + days + "일)");

    $.ajax({
        url: SERVER_URL + "/customer-api/customer/" + data.customerId,
        type: "GET",
        success : function (result){
            $("#hotel-schedule-customer-profileImg").attr("src", result.profileImg);
            $("#hotel-schedule-customer").text(result.name + " (" + result.typeName + " / " + result.telNo + ")");
        }
    });

    for(var idx = 0; idx < data.tags.length; idx++)
    {
        $("#hotel-schedule-tags").append("<span class=\"badge badge-secondary\">" + data.tags[idx].title + "</span>");
    }

    $("#hotel-schedule-bigo").val(data.bigo);
}