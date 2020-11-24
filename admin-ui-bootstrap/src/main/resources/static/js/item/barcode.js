$(function(){

    $.ajax({
        url: SERVER_URL + "/sale-api/item/barcode",
        type: "GET",
        data : {"count" : 10},
        success : function (data){
            var count = data.length;

            for (var idx = 0; idx < count; idx++)
            {
                var canvas = "<div class=\"col-xl-4 col-lg-6 col-md-6 col-sm-12\">";
                    canvas+=    "<img class=\"barcode\"\n" +
                                "  jsbarcode-format=\"CODE128\"\n" +
                                "  jsbarcode-value=\"" + data[idx] + "\"\n" +
                                "  jsbarcode-textmargin=\"0\"\n" +
                                "  jsbarcode-fontoptions=\"bold\"\n" +
                                "</img>"
                    canvas+= "</div>";

                $("#barcode-row").append(canvas);
            }

            JsBarcode(".barcode").init();
        }
    });
});