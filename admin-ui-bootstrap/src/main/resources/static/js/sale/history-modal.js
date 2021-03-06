let historyModalGrid = null;
let historyModalGridData = [];

/**
 * 판매 이력 모달을 오픈한다
 * @param clickData
 */
function openHistoryModal (clickData)
{
    if(historyModalGrid != null)
    {
        historyModalGrid.destroy();
    }

    $.ajax({
        url: "/sale/history/" + clickData.id,
        type: "GET",
    }).done(function(data){
        $("#history-sale-id-input").val(data.id);
        $("#history-modal-form").attr("action", "/sale/refund/" + data.id);
        $("#history-sale-date").text(moment(data.saleDate).format("YYYY-MM-DD HH:mm"))
        $("#history-total-price").text(clickData.total);
        $("#history-payment-type").text(data.paymentTypeName);
        //TODO $("#history-sale-family").text();
        historyModalGridData = data.saleItemList;
        historyModalGridData.filter(function(d){return d.couponId != null; }).forEach(function(d){
            $("#history-sale-coupons").append("<span class=\"badge badge-info\" style=\"margin-right: 5px;\">" + d.couponName + "</span>");
        });

        $('#history-modal').modal({
            show: true
        });
    });
}

function historyModalRefundButtonClick()
{
    var result = confirm("해당 판매 이력을 환불하시겠어요?");
    if(result)
    {
        $("#history-modal-form").submit();
    }
}