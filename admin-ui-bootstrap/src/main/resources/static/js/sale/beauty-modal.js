/**
 * 미용 모달 컴포넌트 초기화 함수
 */
function initBeautyModal ()
{
    // 미용 검색 select 구성
    $("#beauty-select").select2({
        multiple : true,
        placeholder: "어떤 미용을 하셨는 지 선택해주세요.",
        allowClear: true,
        width : "100%",
        minimumInputLength : 1,
        ajax: {
            url: SERVER_URL + "/sale-api/item/beauty",
            method: "GET",
            data : function (params) {return { keyword: params.term , page : params.page || 0};},
            processResults: function (data, params) {
                params.page = data.number || 0;
                var returnArr = [];
                data.content.forEach(item => returnArr.push({
                    "id" : item.id,
                    "text" : item.name + "(" + item.typeName + " / " + item.price + " 원)",
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

    $("#add-beauty-button").on("click", function(e){
        var scheduleId = $("#beauty-id-input").val();
        /** @type Object[] */
        var selectedBeauties = $("#beauty-select").select2('data');
        var selectedCoupon = $("#beauty-modal-form").serializeObject()["beauty-coupons"];

        var beauties = [];
        selectedBeauties.forEach(b => beauties.push(Number(b.id)));

        debugger;

        // 스케쥴 데이터 조회
        $.ajax({
            url: "/sale/cart",
            type: "POST",
            dataType : "JSON",
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify({
                scheduleId : scheduleId,
                beauties : beauties,
                coupon : selectedCoupon
            })
        });
    });
}

/**
 * 미용 모달 오픈 함수
 * @param data
 */
function openBeautyModal (data)
{
    // 모달 데이터 초기화
    resetBeautyModal();

    // 스케쥴 데이터 조회
    $.ajax({
        url: SERVER_URL + "/schedule-api/schedules/" + data.id,
        type: "GET",
        success : function (result){
            // 모달 element에 값 세팅
            setBeautyData(result);
            // 모달 오픈
            $('#beauty-modal').modal({
                show: true
            });
        }
    });
}

/**
 * 미용 모달 데이터 초기화 함수
 */
function resetBeautyModal ()
{
    $("#beauty-id-input").val("");
    $("#beauty-schedule-name").text("");
    $("#beauty-schedule-duration").text("");
    $("#beauty-schedule-customer-profileImg").attr("src", "");
    $("#beauty-schedule-customer").text("");
    $("#beauty-schedule-tags").empty();
    $("#beauty-schedule-bigo").val("");
    $("#beauty-select").val(null).trigger("change");
    if($("#accordion-page-1").attr("aria-expanded") == "false")
    {
        $("#accordion-page-1").click();
    }
}

/**
 * 모달에 미용 데이터 세팅
 * @param data
 */
function setBeautyData (data)
{
    $("#beauty-id-input").val(data.id);
    $("#beauty-schedule-name").text(data.name);
    $("#beauty-schedule-duration").text(data.startDate + " ~ " + data.endDate);

    $.ajax({
        url: SERVER_URL + "/customer-api/customer/" + data.customerId,
        type: "GET",
        success : function (result){
            $("#beauty-schedule-customer-profileImg").attr("src", result.profileImg);
            $("#beauty-schedule-customer").text(result.name + " (" + result.typeName + " / " + result.telNo + ")");
        }
    });

    for(var idx = 0; idx < data.tags.length; idx++)
    {
        $("#beauty-schedule-tags").append("<span class=\"badge badge-secondary\">" + data.tags[idx].title + "</span>");
    }

    $("#beauty-schedule-bigo").val(data.bigo);
}