$(function(){
    $("#item-card").hide();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 초기 데이터 세팅
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 고객 종류
    $.ajax({
        url : "http://1hoon.iptime.org:8366/sale-api/item/type",
        type : "GET",
    }).done((data, textStatus, jqXHR) => {
        $("#type-select").select2({
            placeholder: "상품 종류를 선택해주세요.",
            allowClear: true,
            width : "100%",
            data : data
        });
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 컴포넌트 초기화
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 상품 목록 페이징 컴포넌트 초기화
    const Pagination = new tui.Pagination(document.getElementById('tui-pagination-container'), {
        totalItems : 0,
        itemsPerPage : 10,
        visiblePages : 5,
        centerAlign : true
    });

    // 상품 목록 그리드 세팅
    const grid = new tui.Grid({
        el: document.getElementById('item-grid'),
        scrollX: false,
        scrollY: false,
        selectionUnit : "row",
        columns: [
            {
                header: '고객명',
                name: 'name'
            },
            {
                header: '종류',
                name: 'typeName'
            },
            {
                header: '전화번호',
                name: 'telNo'
            }
        ]
    });
    // 최근 구매자 목록 그리드
    const historyGrid = new tui.Grid({
        el: document.getElementById('recent-buy-history-grid'),
        scrollX: false,
        scrollY: false,
        selectionUnit : "row",
        columns: [
            {
                header: '고객명',
                name: 'name'
            },
            {
                header: '종류',
                name: 'typeName'
            },
            {
                header: '전화번호',
                name: 'telNo'
            }
        ]
    });
    tui.Grid.applyTheme('clean');

    // 상품 이미지 초기화
    cropper = '';
    let $confirmBtn = $("#confirm-button");
    let $resetBtn = $("#reset-button");
    let $cutBtn = $("#cut-button");
    let $newItemImage = $("#new-item-image");
    let $currentItemImage = $("#current-item-image");
    let $resultImage = $("#cropped-new-item-image");
    let $itemImage = $("#itemImg-input");

    $newItemImage.hide();
    $cutBtn.hide();
    $resetBtn.hide();
    $confirmBtn.hide();


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 이벤트
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 상품 검색 버튼 클릭 이벤트
     */
    $("#searchButton").on("click", function(e){
        searchItemList();
    });
    /**
     * 상품 검색 인풋 키입력 이벤트
     */
    $("#searchKeyword").on("keyup", function(e){
        if (e.keyCode == 13)
        {
            searchItemList();
        }
    });

    /**
     * 상품 목록 페이지 이동 이벤트
     */
    Pagination.on("afterMove", function (e){
        var currentPage = e.page;
        moveItemListPage(currentPage);
    });

    /**
     * 상품 목록 그리드 클릭 이벤트
     */
    grid.on("click", function(e){
        if (e.rowKey == undefined || e.rowKey == null)
        {
            return;
        }

        var clickData = grid.getData()[e.rowKey];
        searchItemInfo(clickData.id);
    });

    /**
     * 상품 이미지 변경 버튼 클릭 이벤트
     */
    $("#profile-image-file").change(function(e) {
        if (e.target.files.length === 1) {
            const reader = new FileReader();
            reader.onload = e => {
                if (e.target.result) {
                    if (!e.target.result.startsWith("data:image")) {
                        alert("이미지 파일을 선택하세요.");
                        return;
                    }

                    let img = document.createElement("img");
                    img.id = 'new-item';
                    img.src = e.target.result;
                    img.setAttribute('width', '100%');

                    $newItemImage.html(img);
                    $newItemImage.show();
                    $currentItemImage.hide();

                    let $newImage = $(img);
                    $newImage.cropper({aspectRatio: 1});
                    cropper = $newImage.data('cropper');

                    $cutBtn.show();
                    $confirmBtn.hide();
                    $resetBtn.show();
                }
            };

            reader.readAsDataURL(e.target.files[0]);
        }
    });
    /**
     * 상품 이미지 변경 취소 버튼 클릭 이벤트
     */
    $resetBtn.click(function() {
        $currentItemImage.show();
        $newItemImage.hide();
        $resultImage.hide();
        $resetBtn.hide();
        $cutBtn.hide();
        $confirmBtn.hide();
        $itemImage.val('');
    });
    /**
     * 상품 이미지 변경 영역자르기 버튼 클릭 이벤트
     */
    $cutBtn.click(function () {
        let dataUrl = cropper.getCroppedCanvas().toDataURL();

        if (dataUrl.length > 10 * 1024 * 1024) {
            alert("이미지 파일이 너무 큽니다. 10MB 보다 작은 파일을 사용하세요. 현재 이미지 사이즈 " + dataUrl.length);
            return;
        }

        let newImage = document.createElement("img");
        newImage.id = "cropped-new-item-image";
        newImage.src = dataUrl;
        newImage.width = 125;
        $resultImage.html(newImage);
        $resultImage.show();
        $confirmBtn.show();
        $confirmBtn.click(function () {
            $newItemImage.html(newImage);
            $cutBtn.hide();
            $confirmBtn.hide();
            $("#profile-img").attr("src", dataUrl);
            $itemImage.val(dataUrl.replaceAll("data:image/png;base64,", ""));
        });
    });

    /**
     * 저장 버튼 클릭 이벤트
     */
    $("#submit-button").on("click", function(e){
        var data = $("#item-form").serializeObject();
        $.ajax({
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            method : "PUT",
            url : "http://1hoon.iptime.org:8366/sale-api/item/" + $("#itemId-p").text(),
            data : JSON.stringify(data)
        }).done(function(data, status){
            $newItemImage.hide();
            $cutBtn.hide();
            $resetBtn.hide();
            $confirmBtn.hide();
            $currentItemImage.show();
            $("#item-image-file").val("");
        });
    });


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 함수
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 상품 목록 검색 함수
     */
    function searchItemList ()
    {
        var searchKeyword = $("#searchKeyword").val();

        if (searchKeyword == "")
        {
            alert("검색어를 입력해주세요.");
            return;
        }
        $("#searchKeywordSaved").val(searchKeyword);

        $.ajax({
            url : "http://1hoon.iptime.org:8366/saler-api/item",
            type : "GET",
            data : {"keyword" : searchKeyword}
        }).done((data, textStatus, jqXHR) => {
            grid.resetData(data.content);
            Pagination.reset(data.totalElements);
        });
    }

    /**
     * 상품 목록 페이지 이동 함수
     * @param movePage 이동할 페이지
     */
    function moveItemListPage (movePage)
    {
        var searchKeyword = $("#searchKeywordSaved").val();
        $.ajax({
            url : "http://1hoon.iptime.org:8366/sale-api/item",
            type : "GET",
            data : {"keyword" : searchKeyword, "page" : movePage - 1}
        }).done((data, textStatus, jqXHR) => {
            grid.resetData(data.content);
        });
    }

    /**
     * 상품 정보를 조회하는 함수
     * @param itemId
     */
    function searchItemInfo (itemId)
    {
        $.ajax({
            url : "http://1hoon.iptime.org:8366/sale-api/item/" + itemId,
            type : "GET"
        }).done((data, textStatus, jqXHR) => {
            // 상품 상세 정보 영역 표시
            $("#item-card").show();

            // 상품 상세 정보 세팅
            $("#item-img").attr("src", data.profileImg);
            $("#itemImage").val(data.profileImg);
            $("#itemId-p").text(data.id);
            $("#name-input").val(data.name);
            $("#barcode-input").val(data.barcode);
            $("#type-select").val(data.type);
            $("#type-select").trigger('change');
            $("#price-input").val(data.price);
            $("#unit-input").val(data.unit);
            $("#itemQuantity-input").val(data.itemQuantity);
            $("#stamp-input").val(data.stamp);

            // 최다 구매 고객 조회
            searchTopBuyerList(itemId);

            // 최근 구매 고객 조회
            searchSaleHistory(itemId);
        });
    }

    /**
     * 최다 구매 고객 리스트를 조회하는 함수
     * @param itemId
     */
    function searchTopBuyerList (itemId)
    {
        $.ajax({
            url : "http://1hoon.iptime.org:8366/sale-api/sale",
            type : "GET",
            data : {"itemId" : itemId}
        }).done((data, textStatus, jqXHR) => {
            //TODO 조회 후 카드 생성 로직
        });
    }

    /**
     * 최근 구매 내역 리스트를 조회하는 함수
     * @param itemId
     */
    function searchSaleHistory (itemId)
    {
        $.ajax({
            url : "http://1hoon.iptime.org:8366/sale-api/sale",
            type : "GET",
            data : {"itemId" : itemId}
        }).done((data, textStatus, jqXHR) => {
            historyGrid.resetData(data.content);
        });
    }
});