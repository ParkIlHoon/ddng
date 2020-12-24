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