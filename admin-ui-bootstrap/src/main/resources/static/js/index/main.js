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
        url: "/today-schedules",
        type: "GET",
        data: {"baseDate": searchDate}
    }).done((data, textStatus, jqXHR) => {

        for (var idx = 0; idx < data.length; idx++)
        {
            var appendString = "<tr>\n" +
                "<td>" + data[idx].scheduleTypeName + "</td>\n" +
                "<td>" + data[idx].customerName + "</td>\n" +
                "<td>" + data[idx].customerTypeName + "</td>\n" +
                "<td>" + data[idx].customerTelNo + "</td>\n" +
                "<td>\n" +
                "tags\n" +
                "</td>\n" +
                "</tr>";

            var totalTagString = "";

            if (data[idx].scheduleTags != null)
            {
                for (var tagidx = 0; tagidx < data[idx].scheduleTags.length; tagidx++) {
                    var tagString = "<span class=\"badge badge-secondary\">title</span>\n";
                    tagString = tagString.replaceAll("title", data[idx].scheduleTags[tagidx]);
                    totalTagString += tagString;
                }
            }
            appendString = appendString.replaceAll("tags", totalTagString);

            $("#schedule-table-body").append(appendString);
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