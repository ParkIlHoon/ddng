let saleGrid;
let hotelGrid;
let beautyGrid;
let historyGrid;

$(function(){

    // 컴포넌트 초기화
    initComponents();

    // 스케쥴 데이터 세팅
    getTodaySchedules();


});

/**
 * 화면 내 컴포넌트 초기화 함수
 */
function initComponents()
{
    // 그리드 테마 지정
    tui.Grid.applyTheme('clean');

    // 판매 그리드
    saleGrid = new tui.Grid({
        el: document.getElementById('sale-grid'),
        data : [],
        scrollX: false,
        scrollY: false,
        selectionUnit : "row",
        columns: [
            {
                header: '번호',
                name: 'no'
            },
            {
                header: '상품명',
                name: 'name'
            },
            {
                header: '개수',
                name: 'count'
            },
            {
                header: '금액',
                name: 'amount'
            }
        ]
    });

    // 호텔, 유치원 그리드
    hotelGrid = new tui.Grid({
        el: document.getElementById('hotel-grid'),
        data : [],
        scrollX: false,
        scrollY: false,
        selectionUnit : "row",
        columns: [
            {
                header: '구분',
                name: 'scheduleTypeName'
            },
            {
                header: '고객명',
                name: 'customerName'
            },
            {
                header: '품종',
                name: 'customerTypeName'
            },
            {
                header: '전화번호',
                name: 'customerTelNo'
            },
            {
                header: '시작일자',
                name: 'startDate'
            },
            {
                header: '종료일자',
                name: 'endDate'
            }
        ]
    });

    // 미용 그리드
    beautyGrid = new tui.Grid({
        el: document.getElementById('beauty-grid'),
        data : [],
        scrollX: false,
        scrollY: false,
        selectionUnit : "row",
        columns: [
            {
                header: '고객명',
                name: 'customerName'
            },
            {
                header: '품종',
                name: 'customerTypeName'
            },
            {
                header: '전화번호',
                name: 'customerTelNo'
            }
        ]
    });

    historyGrid = new tui.Grid({
        el: document.getElementById('history-grid'),
        data : [
            {"date" : "2020-09-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-08-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-07-22", "type" : "유치원", "amount" : "10,000"},
            {"date" : "2020-09-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-08-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-07-22", "type" : "유치원", "amount" : "10,000"},
            {"date" : "2020-09-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-08-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-07-22", "type" : "유치원", "amount" : "10,000"},
            {"date" : "2020-09-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-08-19", "type" : "미용", "amount" : "30,000"},
            {"date" : "2020-07-22", "type" : "유치원", "amount" : "10,000"},
            {"date" : "2020-07-11", "type" : "호텔", "amount" : "70,000"}
        ],
        scrollX: false,
        scrollY: true,
        selectionUnit : "row",
        bodyHeight: 300,
        columns: [
            {
                header: '구분',
                name: 'type'
            },
            {
                header: '날짜',
                name: 'date'
            },
            {
                header: '금액',
                name: 'amount'
            }
        ]
    });
}

/**
 * 오늘 스케쥴을 조회해 각 그리드에 세팅해주는 함수
 */
function getTodaySchedules ()
{
    // 오늘 날짜 세팅
    var today = moment().format('YYYY-MM-DD');

    // 그리드 초기화
    hotelGrid.resetData([]);
    beautyGrid.resetData([]);

    $.ajax({
        url: SERVER_URL + "/schedule-api/schedules/day",
        type: "GET",
        data: {"baseDate": today}
    }).done((data, textStatus, jqXHR) => {

        if (data.length > 0)
        {
            var customerIds = [];
            data.forEach(d => customerIds.push(d.customerId));
            var keyword = customerIds.join(",");

            $.ajax({
                url: SERVER_URL + "/customer-api/customer/in",
                type: "GET",
                data : {"keyword" : keyword},
                async: false,
                success : function (customers){
                    var hotelGridData = [];
                    var beautyGridData = [];

                    for (var idx = 0; idx < data.length; idx++)
                    {
                        var schedule = data[idx];
                        var customer = customers.find(el => el.id == schedule.customerId);
                        schedule.customerName = customer.name;
                        schedule.customerTypeName = customer.typeName;
                        schedule.customerTelNo = customer.telNo;

                        if (schedule.scheduleType == "HOTEL" || schedule.scheduleType == "KINDERGARTEN")
                        {
                            hotelGridData.push(schedule);
                        }
                        else if (schedule.scheduleType == "BEAUTY")
                        {
                            beautyGridData.push(schedule);
                        }
                    }

                    hotelGrid.resetData(hotelGridData);
                    beautyGrid.resetData(beautyGridData);
                }
            });
        }

    });
}