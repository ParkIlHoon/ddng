$(function(){
    for (var idx = 0; idx < 10; idx++)
    {
        var canvas = "<div class=\"col-xl-2 col-lg-4 col-md-4 col-sm-6\">";
            canvas+=    "<img class=\"barcode\"\n" +
                        "  jsbarcode-format=\"CODE128\"\n" +
                        "  jsbarcode-value=\"123456789012\"\n" +
                        "  jsbarcode-textmargin=\"0\"\n" +
                        "  jsbarcode-fontoptions=\"bold\"\n" +
                        "</img>"
            canvas+= "</div>";

        $("#barcode-row").append(canvas);
    }

    JsBarcode(".barcode").init();
});