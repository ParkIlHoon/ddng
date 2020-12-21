$(function(){
    $("#family-card").hide();
    var g_familyId;

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

    function deleteAllFamilyCards() {
        $("#search-result-row").empty();
    }

    function openFamilyTab (familyId, familyName)
    {
        g_familyId = familyId;
        $("#family-card").show();
        var offset = $("#family-card").offset();
        $('html, body').animate({scrollTop : offset.top}, 400);
        $("#family-card-title").text(familyName);

        $.ajax({
            url : SERVER_URL + "/customer-api/family/" + familyId,
            type : "GET"
        }).done((data, textStatus, jqXHR) => {
            var customers = data.customers;
            deleteAllMemberCard();
            deleteAllCouponCard();
            createMemberCard(customers);
            createCouponCard(customers.map(customer => customer.id))
            $("#family-name-input").val(data.name);
            $("#family-name-input").removeClass("is-valid");
            $("#family-name-input").removeClass("is-invalid");
            $("#family-name-input-feedback").text("");
        });
    }

    function deleteAllMemberCard()
    {
        $("#member-tab-page-row").empty();
    }

    function deleteAllCouponCard()
    {
        $("#coupon-tab-page-row").empty();
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

    function createCouponCard(customerIds)
    {
        $.ajax({
            url : SERVER_URL + "/sale-api/coupon?customerIds=" + customerIds[0] + "&customerIds=" + customerIds[1] + "&customerIds=" + customerIds[2],
            type : "GET"
        }).done((data, textStatus, jqXHR) => {
            var coupons = data.content;

            for (var idx = 0; idx < coupons.length; idx++)
            {
                var name = coupons[idx].name;
                var typeName = coupons[idx].typeName;
                var itemTypeName = coupons[idx].itemTypeName;
                var createDate = coupons[idx].createDate;
                var expireDate = coupons[idx].expireDate;
                var useDate = coupons[idx].useDate;

                var couponHtml = "<div class=\"col-md-6 col-lg-3\">\n" +
                                    "<div class=\"card\">\n";

                if (useDate == "" || useDate == null)
                {
                    couponHtml +=       "<div class=\"card-body text-center text-white bg-gradient-info\">";
                }
                else
                {
                    couponHtml +=       "<div class=\"card-body text-center\">\n";
                }
                    couponHtml +=           "<div class=\"text-muted small text-uppercase font-weight-bold\">" + createDate + "</div>\n" +
                                            "<div class=\"text-value-xl py-3\">" + name + "(" + itemTypeName + "/" + typeName + ")" + "</div>\n" +
                                            "<div>\n";
                if (useDate == "" || useDate == null)
                {
                    couponHtml +=               "<small class=\"text-muted\">유효기간 : " + expireDate + "</small>\n";
                }
                else
                {
                    couponHtml +=               "<small class=\"text-muted\">사용 완료(" + useDate + ")</small>\n";
                }
                    couponHtml +=          "</div>\n" +
                                        "</div>\n" +
                                    "</div>\n" +
                                 "</div>";

                $("#coupon-tab-page-row").append(couponHtml);
            }
        });
    }

    $("#family-name-input").change(function(){
        var value = $("#family-name-input").val();

        if (value != undefined || value != "")
        {
            $("#family-name-input").removeClass("is-valid");
            $("#family-name-input").removeClass("is-invalid");
            $("#family-name-input-feedback").text("");
        }
    })

    $("#submit-family-name-button").on("click", function(){
        var value = $("#family-name-input").val();

        if (value == undefined || value == "")
        {
            $("#family-name-input").addClass("is-invalid");
            $("#family-name-input-feedback").text("값이 비어있습니다.");
            $("#family-name-input").focus();
            return;
        }

        $.ajax({
            url : SERVER_URL + "/customer-api/family/" + g_familyId,
            type : "PUT",
            data : JSON.stringify({name : value}),
            dataType : "json",
            contentType:"application/json",
            complete : function (){
                $("#family-name-input").addClass("is-valid");
            }
        });
    });
});