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
    /*
     메인 페이지
     */
    // 상품 검색 select 구성
    $("#item-select").select2({
        placeholder: "상품명/바코드",
        allowClear: true,
        width : "100%",
        minimumInputLength : 1,
        ajax: {
            url: SERVER_URL + "/sale-api/item",
            method: "GET",
            data : function (params) {return { keyword: params.term , page : params.page || 0};},
            processResults: function (data, params) {
                params.page = data.number || 0;
                var returnArr = [];
                data.content.forEach(item => returnArr.push({
                    "id" : item.id,
                    "text" : item.name + "(" + item.typeName + " / " + item.price + " 원 / " + item.barcode + ")",
                    "name" : item.name,
                    "price" : item.price
                }));
                return {
                    results: returnArr,
                    pagination: {
                        more: (params.page * 10) < data.totalElements
                    }
                };
            }
        }
    });

    // 그리드 테마 지정
    tui.Grid.applyTheme('clean');

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
    hotelGrid.on("click", function(e){
        if (e.rowKey == undefined || e.rowKey == null) {
            return;
        }
        var clickData = hotelGrid.getData()[e.rowKey];
        if (clickData.scheduleType == "HOTEL")
        {
            openHotelModal(clickData);
        }
        else
        {
            openKindergartenModal(clickData);
        }
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
    beautyGrid.on("click", function(e){
        if (e.rowKey == undefined || e.rowKey == null) {
            return;
        }
        var clickData = beautyGrid.getData()[e.rowKey];
        openBeautyModal(clickData);
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

    /*
     미용 모달
     */
    initBeautyModal();

    /*
     호텔 모달
     */
    initHotelModal();

    /*
     유치원 모달
     */
    initKindergartenModal();
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

/**
 * 상품 select 선택 이벤트 핸들러
 */
$('#item-select').on('select2:select', function (e) {
    var data = e.params.data;

    //TODO 상품 데이터 세팅
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
        });

        // 총 금액 세팅
        // var totalPrice = 0;
        // saleGridData.forEach(el => totalPrice += el.amount * el.count);
        // $("#total-price").text(totalPrice);

        // 상품 검색 select 초기화
        $('#item-select').val(null).trigger('change');
    }
});

/**
 * 현금 결제 버튼 클릭 핸들러
 */
$("#pay-cash-button").on("click", function(e){
    //TODO 결제
});
/**
 * 카드 결제 버튼 클릭 핸들러
 */
$("#pay-card-button").on("click", function(e){
    //TODO 결제
});
/**
 * 초기화 버튼 클릭 핸들러
 */
$("#reset-button").on("click", function(e){
    $("#total-price").text(0);
});
/**
 * 결제취소 버튼 클릭 핸들러
 */
$("#cancel-button").on("click", function(e){
    //TODO 결제 취소
});