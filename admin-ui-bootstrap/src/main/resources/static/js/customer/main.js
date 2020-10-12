$(function(){
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 전역변수
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    var g_tagify;
    $("#customer-card").hide();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 초기 데이터 세팅
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 고객 종류
    $.ajax({
        url : "http://localhost:8090/customer-api/customer/type",
        type : "GET",
    }).done((data, textStatus, jqXHR) => {
        $("#type-select").select2({
            placeholder: "종류를 선택해주세요.",
            allowClear: true,
            data : data
        });
    });
    // 태그
    $.ajax({
        url : "http://localhost:8090/customer-api/tag",
        type : "GET",
    }).done((data, textStatus, jqXHR) => {
        var whiteList = [];
        data.forEach(function(item){
            whiteList.push(item.title);
        });

        var tagInput = document.querySelector("#tags-input");
        g_tagify = new Tagify(tagInput, {
            pattern: /^.{0,20}$/,
            whitelist : whiteList,
            dropdown : {
                position: "input",
                enabled: 1, // suggest tags after a single character input
                fuzzySearch: false
            }, // map tags
            backspace : false
        });

        g_tagify.on("add", onAdd);
        g_tagify.on("remove", onRemove);
        g_tagify.DOM.input.classList.add('form-control');
        g_tagify.DOM.scope.parentNode.insertBefore(g_tagify.DOM.input, g_tagify.DOM.scope);
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 컴포넌트 초기화
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    const grid = new tui.Grid({
        el: document.getElementById('grid'),
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

    const Pagination = new tui.Pagination(document.getElementById('tui-pagination-container'), {
        totalItems : 0,
        itemsPerPage : 10,
        visiblePages : 5,
        centerAlign : true
    });

    cropper = '';
    let $confirmBtn = $("#confirm-button");
    let $resetBtn = $("#reset-button");
    let $cutBtn = $("#cut-button");
    let $newProfileImage = $("#new-profile-image");
    let $currentProfileImage = $("#current-profile-image");
    let $resultImage = $("#cropped-new-profile-image");
    let $profileImage = $("#profileImage");

    $newProfileImage.hide();
    $cutBtn.hide();
    $resetBtn.hide();
    $confirmBtn.hide();

    const historyGrid = new tui.Grid({
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
                header: '날짜',
                name: 'date'
            },
            {
                header: '구분',
                name: 'type'
            },
            {
                header: '금액',
                name: 'amount'
            }
        ]
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 이벤트
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    $("#searchButton").on("click", function(e){
        searchCustomerList();
    });
    $("#searchKeyword").on("keyup", function(e){
        if (e.keyCode == 13)
        {
            searchCustomerList();
        }
    });
    grid.on("click", function(e){
        if (e.rowKey == undefined || e.rowKey == null)
        {
            return;
        }

        var clickData = grid.getData()[e.rowKey];

        $.ajax({
            url : "http://localhost:8090/customer-api/customer/" + clickData.id,
            type : "GET"
        }).done((data, textStatus, jqXHR) => {
            $("#customer-card").show();
            $("#profile-img").attr("src", data.profileImg);
            $("#profileImage").val(data.profileImg);
            $("#customer-id").text(data.id);
            $("#name-input").val(data.name);
            $("#family-p").text(data.familyString);
            $("#type-select").val(data.type);
            $("#type-select").trigger('change');;
            $("#telNo-input").val(data.telNo);
            $("#date-input").val(moment(data.joinDate, "YYYYMMDD").format("YYYY-MM-DD"));
            $("#bigo-input").val(data.bigo);

            var value = "";
            for (var idx = 0; idx < data.tags.length; idx++)
            {
                if(value != "") value += ",";
                value += data.tags[idx].title;
            }
            $("#tags-input").val("");
            $("#tags-input").val(value);

            if(g_tagify != null)
            {
                g_tagify.removeAllTags();
                g_tagify.loadOriginalValues(value);
            }

            historyGrid.refreshLayout();
        });
    });
    Pagination.on("afterMove", function (e){
        var currentPage = e.page;
        moveCustomerListPage(currentPage);
    });

    $("#submit-button").on("click", function(e){
        debugger;
        var data = $("#customer-form").serializeObject();
        $.ajax({
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            method : "PUT",
            url : "http://localhost:8090/customer-api/customer/" + $("#customer-id").text(),
            data : JSON.stringify(data)
        }).done(function(data, status){
            console.log("${data} and status is ${status}");
            $newProfileImage.hide();
            $cutBtn.hide();
            $resetBtn.hide();
            $confirmBtn.hide();
            $currentProfileImage.show();
            $("#profile-img").attr("src", $("#profileImage").val());
            $("#profile-image-file").val("");
        });
    });
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
                    img.id = 'new-profile';
                    img.src = e.target.result;
                    img.setAttribute('width', '100%');

                    $newProfileImage.html(img);
                    $newProfileImage.show();
                    $currentProfileImage.hide();

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

    $resetBtn.click(function() {
        $currentProfileImage.show();
        $newProfileImage.hide();
        $resultImage.hide();
        $resetBtn.hide();
        $cutBtn.hide();
        $confirmBtn.hide();
        $profileImage.val('');
    });

    $cutBtn.click(function () {
        let dataUrl = cropper.getCroppedCanvas().toDataURL();

        if (dataUrl.length > 1000 * 1024) {
            alert("이미지 파일이 너무 큽니다. 1024000 보다 작은 파일을 사용하세요. 현재 이미지 사이즈 " + dataUrl.length);
            return;
        }

        let newImage = document.createElement("img");
        newImage.id = "cropped-new-profile-image";
        newImage.src = dataUrl;
        newImage.width = 125;
        $resultImage.html(newImage);
        $resultImage.show();
        $confirmBtn.show();
        $confirmBtn.click(function () {
            $newProfileImage.html(newImage);
            $cutBtn.hide();
            $confirmBtn.hide();
            $profileImage.val(dataUrl);
        });
    });



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 함수
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    function getTagList ()
    {
        $.ajax({
            url : "http://localhost:8090/customer-api/tag",
            type : "GET",
        }).done((data, textStatus, jqXHR) => {
            var whiteList = [];
            data.forEach(function(item){
                whiteList.push(item.title);
            });
            g_tagify.settings.whitelist.length = 0;
            g_tagify.settings.whitelist.splice(0, whiteList.length, ...whiteList);
        });
    }

    function searchCustomerList ()
    {
        var searchKeyword = $("#searchKeyword").val();

        if (searchKeyword == "")
        {
            alert("검색어를 입력해주세요.");
            return;
        }
        $("#searchKeywordSaved").val(searchKeyword);

        $.ajax({
            url : "http://localhost:8090/customer-api/customer",
            type : "GET",
            data : {"keyword" : searchKeyword}
        }).done((data, textStatus, jqXHR) => {
            grid.resetData(data.content);

            Pagination.reset(data.totalElements);
            // Pagination.movePageTo(movePage);
        });
    }

    function moveCustomerListPage (movePage)
    {
        var searchKeyword = $("#searchKeywordSaved").val();
        $.ajax({
            url : "http://localhost:8090/customer-api/customer",
            type : "GET",
            data : {"keyword" : searchKeyword, "page" : movePage - 1}
        }).done((data, textStatus, jqXHR) => {
            grid.resetData(data.content);
        });
    }

    function onAdd(e)
    {
        tagRequest("POST", e.detail.data.value);
    }
    function onRemove(e)
    {
        tagRequest("DELETE", e.detail.data.value);
    }
    function tagRequest(method, title)
    {
        $.ajax({
            dataType : "json",
            autocomplete : {
                enable : true,
                rightKey : true
            },
            contentType : "application/json; charset=utf-8",
            method : method,
            url : "http://localhost:8090/customer-api/customer/" + $("#customer-id").text() + "/tag",
            data : JSON.stringify({"title" : title}),
        });
    }
});