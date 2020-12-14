let datepicker;

$(function(){

    Number.prototype.format = function(){
        if(this==0) return 0;

        var reg = /(^[+-]?\d+)(\d{3})/;
        var n = (this + '');

        while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

        return n;
    };

    var container = document.getElementById('tui-date-picker-container');
    datepicker = new tui.DatePicker(container, {
        language : "ko",
        date : new Date(),
        showAlways : true
    });
    datepicker.on('change', function() {
        drawChartByItem(datepicker.getDate());
        drawChartByPayment(datepicker.getDate());
    });

    drawChartByItem(datepicker.getDate());
    drawChartByPayment(datepicker.getDate());
});

function drawChartByItem(date)
{
    var today = moment(date).format('YYYY-MM-DD');
    var container = document.getElementById("chart-area-by-item");
    var chartData = {
        categories: ['상품 종류'],
        seriesAlias: {
            pie1: 'pie',
            pie2: 'pie'
        },
        series: {
            pie1: [],
            pie2: []
        }
    };
    var options = {
        chart: {
            width: 600,
            height: 560,
            title: '상품 분류별 분석'
        },
        series: {
            pie1: {
                radiusRange: ['57%'],
                labelAlign: 'center',
                showLegend: true
            },
            pie2: {
                radiusRange: ['70%', '100%'],
                labelAlign: 'outer',
                showLegend: true
            }
        },
        legend: {
            visible: false
        },
        tooltip: {
            suffix: ''
        },
        theme: "comboChartTheme"
    };

    $.ajax({
        url: SERVER_URL + "/sale-api/sale/calculate/item",
        type: "GET",
        data: {"baseDate": today}
    }).done((data, textStatus, jqXHR) => {

        var totalCount = 0;
        var totalAmount = 0;
        $("#cal-item-list").empty();

        if (data.length > 0)
        {
            for (var idx = 0; idx < data.length; idx++)
            {
                chartData.series.pie1.push({
                    name: data[idx].itemTypeName,
                    data: data[idx].count
                });
                chartData.series.pie2.push({
                    name: data[idx].itemTypeName,
                    data: data[idx].amount
                });

                totalCount += data[idx].count;
                totalAmount += data[idx].amount;

                $("#cal-item-list").append("<tr><td>" + data[idx].itemTypeName + "</td><td>" + Number(data[idx].count).format() + "</td><td>" + Number(data[idx].amount).format() + "</td></tr>");
            }
            tui.chart.comboChart(container, chartData, options);
        }
        else
        {
            $("#chart-area-by-item").empty();
        }

        $("#cal-item-total-count").text(Number(totalCount).format() + " 건");
        $("#cal-item-total-amount").text(Number(totalAmount).format() + " 원");
    });
}

function drawChartByPayment(date)
{
    var today = moment(date).format('YYYY-MM-DD');
    var container = document.getElementById("chart-area-by-payment");
    var chartData = {
        categories: ['결제 수단'],
        seriesAlias: {
            pie1: 'pie',
            pie2: 'pie'
        },
        series: {
            pie1: [],
            pie2: []
        }
    };
    var options = {
        chart: {
            width: 600,
            height: 560,
            title: '결제 수단별 분석'
        },
        series: {
            pie1: {
                radiusRange: ['57%'],
                labelAlign: 'center',
                showLegend: true
            },
            pie2: {
                radiusRange: ['70%', '100%'],
                labelAlign: 'outer',
                showLegend: true
            }
        },
        legend: {
            visible: false
        },
        tooltip: {
            suffix: ''
        },
        theme: "comboChartTheme"
    };

    $.ajax({
        url: SERVER_URL + "/sale-api/sale/calculate/payment",
        type: "GET",
        data: {"baseDate": today}
    }).done((data, textStatus, jqXHR) => {

        var totalCount = 0;
        var totalAmount = 0;
        $("#cal-payment-list").empty();

        if (data.length > 0)
        {
            for (var idx = 0; idx < data.length; idx++)
            {
                chartData.series.pie1.push({
                    name: data[idx].paymentTypeName,
                    data: data[idx].count
                });
                chartData.series.pie2.push({
                    name: data[idx].paymentTypeName,
                    data: data[idx].amount
                });

                totalCount += data[idx].count;
                totalAmount += data[idx].amount;

                $("#cal-payment-list").append("<tr><td>" + data[idx].paymentTypeName + "</td><td>" + Number(data[idx].count).format() + "</td><td>" + Number(data[idx].amount).format() + "</td></tr>");
            }
            tui.chart.comboChart(container, chartData, options);
        }
        else
        {
            $("#chart-area-by-payment").empty();
        }

        $("#cal-payment-total-count").text(Number(totalCount).format() + " 건");
        $("#cal-payment-total-amount").text(Number(totalAmount).format() + " 원");
    });
}