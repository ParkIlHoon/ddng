'use strict';

/* eslint-disable require-jsdoc, no-unused-vars */

/**
 * 스케쥴 타입 목록
 * @type {ScheduleType[]}
 */
var ScheduleTypes = [];

/**
 * 스케쥴 타입
 * @see com.ddng.scheduleapi.modules.schedules.domain.ScheduleType
 * @constructor
 */
function ScheduleType()
{
    /**
     * 스케쥴 타입 아이디
     * @type {null}
     */
    this.id = null;
    /**
     * 스케쥴 타입 이름
     * @type {null}
     */
    this.name = null;
    /**
     * 첫 화면 로딩 시 캘린더에 표시 여부
     * @type {boolean}
     */
    this.checked = true;
    /**
     * 스케쥴 텍스트 색상
     * @type {null}
     */
    this.color = "black";
    /**
     * 스케쥴 배경 색상
     * @type {null}
     */
    this.bgColor = null;
    /**
     * 스케쥴 외곽선 색상
     * @type {null}
     */
    this.borderColor = null;
    /**
     * 스케쥴 드래그 버튼 색상
     * @type {null}
     */
    this.dragBgColor = null;
}

/**
 * 스케쥴 타입 초기화 함수
 * @param data 초기화 데이터
 */
function initializeScheduleType (data)
{
    ScheduleTypes = [];

    for (var idx = 0; idx < data.length; idx++)
    {
        var typeId = data[idx].id;
        var typeName = data[idx].name;
        var typeColor = data[idx].color;

        var newType = new ScheduleType();
        newType.id = typeId;
        newType.name = typeName;
        newType.bgColor = typeColor;
        newType.borderColor = typeColor;
        newType.dragBgColor = typeColor;

        addScheduleType(newType);
    }

    var calendarList = document.getElementById('calendarList');
    var html = [];
    ScheduleTypes.forEach(function(scheduleType) {
        html.push('<div class="lnb-calendars-item"><label>' +
            '<input type="checkbox" class="tui-full-calendar-checkbox-round" value="' + scheduleType.id + '" checked>' +
            '<span style="border-color: ' + scheduleType.borderColor + '; background-color: ' + scheduleType.borderColor + ';"></span>' +
            '<span>' + scheduleType.name + '</span>' +
            '</label></div>'
        );
    });
    calendarList.innerHTML = html.join('\n');
}

/**
 * 스케쥴 타입을 추가한다.
 * @param type 추가할 스케쥴 타입
 */
function addScheduleType(type)
{
    ScheduleTypes.push(type);
}

/**
 * 스케쥴 타입을 찾는다.
 * @param id 찾을 스케쥴 타입의 아이디
 * @returns {*|ScheduleType}
 */
function findScheduleType(id)
{
    var found;

    ScheduleTypes.forEach(function(type) {
        if (type.id === id) {
            found = type;
        }
    });

    return found || ScheduleTypes[0];
}

function hexToRGBA(hex)
{
    var radix = 16;
    var r = parseInt(hex.slice(1, 3), radix),
        g = parseInt(hex.slice(3, 5), radix),
        b = parseInt(hex.slice(5, 7), radix),
        a = parseInt(hex.slice(7, 9), radix) / 255 || 1;
    var rgba = 'rgba(' + r + ', ' + g + ', ' + b + ', ' + a + ')';

    return rgba;
}
