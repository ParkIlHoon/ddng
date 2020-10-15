$(function(){
    $("#family-card").hide();

    const Pagination = new tui.Pagination(document.getElementById('tui-pagination-container'), {
        totalItems : 0,
        itemsPerPage : 12,
        visiblePages : 5,
        centerAlign : true
    });

    Pagination.on("afterMove", function (e){
        var currentPage = e.page;
        moveFamilyListPage(currentPage);
    });

    $("#keyword-input").on("keyup", function(e){
        if (e.keyCode == 13)
        {
            searchFamilies();
        }
    });

    $("#search-button").on("click", function(e){
        searchFamilies();
    });

    function searchFamilies ()
    {
        var searchKeyword = $("#keyword-input").val();
        $("#keyword-input-hidden").val(searchKeyword);

        if(searchKeyword == undefined || searchKeyword == "")
        {
            alert("검색어를 입력해주세요.");
            return;
        }

        $.ajax({
            url : "http://localhost:8090/customer-api/family",
            type : "GET",
            data : {"keyword" : searchKeyword, "size" : 12}
        }).done((data, textStatus, jqXHR) => {
            deleteAllMemberCard();
            deleteAllFamilyCards();
            createFamilyCards(data);
            Pagination.reset(data.totalElements);
            $("#family-card").hide();
        });
    }

    function moveFamilyListPage (movePage)
    {
        var searchKeyword = $("#keyword-input-hidden").val();
        $.ajax({
            url : "http://localhost:8090/customer-api/family",
            type : "GET",
            data : {"keyword" : searchKeyword, "page" : movePage - 1, "size" : 12}
        }).done((data, textStatus, jqXHR) => {
            deleteAllFamilyCards();
            createFamilyCards(data);
        });
    }

    function createFamilyCards(data)
    {
        var families = data.content;

        for (var idx = 0; idx < families.length; idx++)
        {
            var childEle = "<div class='col-sm-12 col-md-6 col-lg-6 col-xl-3'>";
                childEle +=    "<div class='card family-card' id='family-card-" + families[idx].id + "' name='" + families[idx].name + "'>\n";
                childEle +=        "<div class='card-body'>\n";
                childEle +=            "<div class='text-value-lg'>" + families[idx].name + "</div>\n";

            var customers = families[idx].customers;
            for (var idx2 = 0; idx2 < customers.length; idx2++)
            {
                childEle +=                "<small>" + customers[idx2].name + " \n";
                childEle +=                    "<div class='c-avatar'>\n";
                childEle +=                        "<img class='c-avatar-img' src='" + customers[idx2].profileImg + "'>\n";
                childEle +=                    "</div>\n";
                childEle +=                "</small>";
            }
                childEle +=         "</div>";
                childEle +=     "</div>";
                childEle += "</div>";

            $("#search-result-row").append(childEle);
            $(".family-card").on("click", function(e){
                var cards = $(".family-card");
                for (var idx = 0; idx < cards.length; idx++)
                {
                    cards[idx].classList.remove("active");
                }

                var card = e.currentTarget;
                card.classList.add("active");
                openFamilyTab(card.id.replaceAll("family-card-", ""), card.getAttribute("name"));
            });
        }
    }

    function deleteAllFamilyCards() {
        $("#search-result-row").empty();
    }

    function openFamilyTab (familyId, familyName)
    {
        $("#family-card").show();
        var offset = $("#family-card").offset();
        $('html, body').animate({scrollTop : offset.top}, 400);
        $("#family-card-title").text(familyName);

        $.ajax({
            url : "http://localhost:8090/customer-api/family/" + familyId,
            type : "GET"
        }).done((data, textStatus, jqXHR) => {
            var customers = data.customers;
            deleteAllMemberCard();
            createMemberCard(customers);
        });
    }

    function deleteAllMemberCard()
    {
        $("#member-tab-page-row").empty();
    }

    function createMemberCard(data)
    {
        for (var idx = 0; idx < data.length; idx++)
        {
            var name = data[idx].name;
            var type = data[idx].type;
            var telNo = data[idx].telNo;
            var profileImg = data[idx].profileImg;
            var tags = data[idx].tags;

            var card = "<div class=\"col-md-6\">\n" +
                            "<div class=\"card overflow-hidden\">\n" +
                                "<div class=\"card-body p-0 d-flex align-items-center\">\n" +
                                    "<div class=\"py-1 px-1 mfe-3\">\n" +
                                        "<div style=\"width: 140px; height: 140px;\">\n" +
                                            "<img style=\"width: 100%;\" src=\"" + profileImg + "\">\n" +
                                        "</div>\n" +
                                    "</div>\n" +
                                    "<div>\n" +
                                        "<div class=\"text-value text-primary\">" + name + "</div>\n" +
                                        "<div class=\"text-muted text-uppercase font-weight-bold small\">" + telNo + "</div>\n" +
                                        "<div class=\"text-muted text-uppercase font-weight-bold small\">" + type + "</div>\n" +
                                        "<div class=\"text-muted text-uppercase font-weight-bold small\">";

            for (var idx2 = 0; idx2 < tags.length; idx2++)
            {
                var tagName = tags[idx2].title;
                card +=                     "<span class=\"badge badge-secondary\">" + tagName + "</span>\n";
            }

            card +=                    "</div>\n" +
                                    "</div>\n" +
                                "</div>\n" +
                            "</div>\n" +
                        "</div>";

            $("#member-tab-page-row").append(card);
        }
    }
});