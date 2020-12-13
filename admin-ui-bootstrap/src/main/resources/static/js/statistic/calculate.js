$(function(){

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
    var container = document.getElementById("chart-area-by-item");
    var data = {
        categories: ['상품 종류'],
        series: [
            {
                name: '간식',
                data: 10
            },
            {
                name: '사료',
                data: 2
            },
            {
                name: '용품',
                data: 5
            },
            {
                name: '미용',
                data: 6
            },
            {
                name: '호텔',
                data: 2
            },
            {
                name: '유치원',
                data: 1
            }
        ]
    };
    var options = {
        chart: {
            width: 600,
            height: 560,
            title: '상품 분류별 분석'
        },
        series : {
            showLabel : true
        },
        tooltip: {
            suffix: '건'
        }
    };
    var theme = {
        series: {
            series: {
                colors: [
                    '#83b14e', '#458a3f', '#295ba0', '#2a4175', '#289399',
                    '#289399', '#617178', '#8a9a9a', '#516f7d', '#dddddd'
                ]
            },
            label: {
                color: '#000000',
                fontFamily: 'Noto Sans',
                borderColor: '#8e6535',
                selectionColor: '#cccccc',
                startColor: '#efefef',
                endColor: 'blue',
                overColor: 'yellow'
            }
        }
    };

// For apply theme

    tui.chart.registerTheme('myTheme', theme);
    options.theme = 'myTheme';

    tui.chart.pieChart(container, data, options);
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
        }
    };
    var theme = {
        series: {
            series: {
                colors: [
                    '#83b14e', '#458a3f', '#295ba0', '#2a4175', '#289399',
                    '#289399', '#617178', '#8a9a9a', '#516f7d', '#dddddd'
                ]
            },
            label: {
                color: '#000000',
                fontFamily: 'Noto Sans',
                borderColor: '#8e6535',
                selectionColor: '#cccccc',
                startColor: '#efefef',
                endColor: 'blue',
                overColor: 'yellow'
            }
        }
    };

// For apply theme

    tui.chart.registerTheme('myTheme', theme);
    options.theme = 'myTheme';

    tui.chart.pieChart(container, data, options);
}