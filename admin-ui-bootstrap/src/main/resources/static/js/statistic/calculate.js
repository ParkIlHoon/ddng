$(function(){

    Number.prototype.format = function(){
        if(this==0) return 0;

        var reg = /(^[+-]?\d+)(\d{3})/;
        var n = (this + '');

        while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

        return n;
    };

    drawChartByItem();

    drawChartByPayment();

    var container = document.getElementById('tui-date-picker-container');
    var target = document.getElementById('tui-date-picker-target');

    var instance = new tui.DatePicker(container, {
        language : "ko",
        date : new Date(),
        input: {
            element: target
        }
    });

    instance.getDate();


});

function drawChartByItem()
{
    var today = moment().format('YYYY-MM-DD');

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
        url: SERVER_URL + "/sale-api/sale/calculate",
        type: "GET",
        data: {"baseDate": today}
    }).done((data, textStatus, jqXHR) => {

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
                })
            }
        }
        tui.chart.comboChart(container, chartData, options);
    });
}

function drawChartByPayment()
{
    var container = document.getElementById("chart-area-by-payment");
    var data = {
        categories: ['결제 수단'],
        series: [
            {
                name: '카드',
                data: 10
            },

            {
                name: '현금',
                data: 5
            },
            {
                name: '외상',
                data: 1
            }
        ]
    };
    var options = {
        chart: {
            width: 600,
            height: 560,
            title: '결제 수단별 분석'
        },
        series : {
            showLabel : true
        },
        tooltip: {
            suffix: '건'
        },
        theme: "comboChartTheme"
    };

    tui.chart.pieChart(container, data, options);
}