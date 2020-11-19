$(function(){
    // 오늘 날짜 세팅
    var today = moment().format('YYYY-MM-DD');
    $("#date-div").text(today);

    // 스케쥴 목록 조회
    getSchedules(today);
});


function getSchedules (searchDate) {
    $("#schedule-table-body").empty();

    $.ajax({
        url: SERVER_URL + "/schedule-api/schedules/day",
        type: "GET",
        data: {"baseDate": searchDate}
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
                    for (var idx = 0; idx < data.length; idx++)
                    {
                        var appendString = "<tr>\n" +
                            "<td>scheduleTypeName</td>\n" +
                            "<td>customerName</td>\n" +
                            "<td>customerTypeName</td>\n" +
                            "<td>customerTelNo</td>\n" +
                            "<td>\n" +
                            "tags\n" +
                            "</td>\n" +
                            "</tr>";

                        var schedule = data[idx];
                        appendString = appendString.replaceAll("scheduleTypeName", schedule.scheduleTypeName);

                        var customer = customers.find(el => el.id == schedule.customerId);

                        appendString = appendString.replaceAll("customerName", customer.name);
                        appendString = appendString.replaceAll("customerTypeName", customer.typeName);
                        appendString = appendString.replaceAll("customerTelNo", customer.telNo);

                        var totalTagString = "";
                        for (var tagidx = 0; tagidx < schedule.tags.length; tagidx++) {
                            var tagString = "<span class=\"badge badge-secondary\">title</span>\n";
                            tagString = tagString.replaceAll("title", schedule.tags[tagidx].title);
                            totalTagString += tagString;
                        }

                        appendString = appendString.replaceAll("tags", totalTagString);

                        $("#schedule-table-body").append(appendString);
                    }
                }
            });
        }
    });
}


$('input[name="certainScheduleOption"]').change(function() {
    // 모든 radio를 순회한다.
    $('input[name="certainScheduleOption"]').each(function() {
        var value = $(this).val();
        var checked = $(this).prop('checked');

        if(checked) {
            var selectId = $(this).prop("id");

            // 기준일자 세팅
            var baseDate;
            switch (selectId)
            {
                case "yesterday" :
                    baseDate = moment().subtract(1, 'days').format('YYYY-MM-DD');
                    break;
                case "today" :
                    baseDate = moment().format('YYYY-MM-DD');
                    break;
                case "tomorrow" :
                    baseDate = moment().add(1, 'days').format('YYYY-MM-DD');
                    break;
            }
            $("#date-div").text(baseDate);

            // 스케쥴 목록 조회
            getSchedules(baseDate);
        }
    });
});