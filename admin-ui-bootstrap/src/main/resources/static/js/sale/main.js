let hotelGrid;
let beautyGrid;
let historyGrid;
let barcode = "";

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
        $("#item-list").replaceWith(jqXHR);
        // 총 금액 세팅
        refreshTotalPrice();
    });
});

/**
 * 카트에 담은 상품을 제거한다.
 * @param itemId 제거할 상품의 아이디
 */
function removeItemFromCartbutton_click (itemId)
{
    $.ajax({
        url: "/sale/cart/" + itemId,
        type: "DELETE",
        contentType : "application/json; charset=utf-8",
    }).always(function (jqXHR) {
        $("#item-list").replaceWith(jqXHR);
        // 총 금액 세팅
        refreshTotalPrice();
    });
}


function window_keydown (e)
{
    var key = e.key;

    if(0 <= key  && key <= 9)
    {
        barcode += key;
    }
    else
    {
        if(key == "Enter" && barcode != "")
        {
            $.ajax({
                url: "/sale/items",
                method: "GET",
                data : {
                    keyword : barcode, page : 0, size : 10
                }
            }).then(function (data) {

                barcode = "";

                var returnArr = [];
                data.content.forEach(item => returnArr.push({
                    "id" : item.id,
                    "text" : item.name + "(" + item.typeName + " / " + item.price + " 원 / " + item.barcode + ")",
                    "name" : item.name,
                    "price" : item.price
                }));

                for (var idx = 0; idx < returnArr.length; idx++)
                {
                    var option = new Option(returnArr[idx].text, returnArr[idx].id, true, true);
                    $("#item-select").append(option).trigger('change');
                }

                $("#item-select").trigger({
                    type: 'select2:select',
                    params: {
                        data: returnArr[returnArr.length - 1]
                    }
                });
            });
        }
        else
        {
            barcode = "";
        }
    }
}