let hotelGrid;
let beautyGrid;
let historyGrid;

/**
 * 카트 총 금액 새로고침 함수
 */
function refreshTotalPrice ()
{
    $.ajax({
        url: "/sale/cart/totalPrice",
        type: "GET",
    }).always(function (responseText) {
        $("#total-price").replaceWith(responseText);
    });
}

/**
 * 결제 함수
 * @param payment 결제 수단(CASH/CARD/CREDIT)
 */
function saleCart (payment)
{
    $("#sale-form-saleType").val("PAYED");
    $("#sale-form-paymentType").val(payment);
    $("#sale-form").submit();
}

/**
 * 상품 select 선택 이벤트 핸들러
 */
$('#item-select').on('select2:select', function (e) {
    var data = e.params.data;

    if (data != undefined && data != "")
    {
        // 장바구니에 추가
        $.ajax({
            url: "/sale/cart",
            type: "POST",
            dataType : "JSON",
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify({
                scheduleId : null,
                itemIds : [data.id],
                couponId : null
            })
        }).always(function (jqXHR) {
            $("#item-list").replaceWith(jqXHR.responseText);
            // 총 금액 세팅
            refreshTotalPrice();
        });

        // 상품 검색 select 초기화
        $('#item-select').val(null).trigger('change');
    }
});
/**
 * 현금 결제 버튼 클릭 핸들러
 */
$("#pay-cash-button").on("click", function(e){
    saleCart("CASH");
});
/**
 * 카드 결제 버튼 클릭 핸들러
 */
$("#pay-card-button").on("click", function(e){
    saleCart("CARD");
});
/**
 * 초기화 버튼 클릭 핸들러
 */
$("#reset-button").on("click", function(e){
    $("#total-price").text(0);
    // 장바구니 초기화
    $.ajax({
        url: "/sale/cart",
        type: "DELETE",
        contentType : "application/json; charset=utf-8",
    }).always(function (jqXHR) {
        $("#item-list").replaceWith(jqXHR.responseText);
        // 총 금액 세팅
        refreshTotalPrice();
    });
});