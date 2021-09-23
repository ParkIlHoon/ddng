var commonUtil = (function(){

    var _moment;

    var init = function(moment) {
        _moment = moment.locale('ko');
    }

    var convertDateTime = function (plainDate) {
        return moment(plainDate).format('LLL');
    }

    var convertDateTimeFromNow = function (plainDate) {
        return moment(plainDate).fromNow();
    }

    return {
        init : init,

        date : {
            convertDateTime : convertDateTime,
            convertDateTimeFromNow : convertDateTimeFromNow
        }
    }
})();