'use strict';

/* eslint-disable */
/* eslint-env jquery */
/* global moment, tui, chance */
/* global findCalendar, CalendarList, ScheduleList, generateSchedule */

(function(window, Calendar) {
    var cal, resizeThrottled;
    var datePicker, selectedCalendar;
    // var g_tagify;

    // 스케쥴 타입 목록 조회
    $.ajax({
        url : "http://1hoon.iptime.org:8366/schedule-api/schedules/types",
        type : "GET",
    }).done((data, textStatus, jqXHR) => {
        // 스케쥴 타입 목록 초기화
        initializeScheduleType(data);
        // 스케쥴 팝업 초기화
        initializePopup(data);

        // 캘린더 초기화
        cal = new Calendar('#calendar', {
            defaultView: 'month',
            taskView : false,
            useCreationPopup: false,
            useDetailPopup: false,
            calendars: ScheduleTypes,
            template: {
                allday: function(schedule) {
                    return getTimeTemplate(schedule, true);
                },
                time: function(schedule) {
                    return getTimeTemplate(schedule, false);
                }
            }
        });

        // 캘린더 이벤트 핸들러 등록
        cal.on({
            'clickMore': function(e) {
                console.log('clickMore', e);
            },
            'clickSchedule': function(e) {
                console.log('clickSchedule', e);
            },
            'clickDayname': function(date) {
                console.log('clickDayname', date);
            },
            'beforeCreateSchedule': function(e) {
                console.log('beforeCreateSchedule', e);
                createNewSchedule(e);
            },
            'beforeUpdateSchedule': function(e) {
                var schedule = e.schedule;
                var changes = e.changes;

                console.log('beforeUpdateSchedule', e);

                if (changes && !changes.isAllDay && schedule.category === 'allday') {
                    changes.category = 'time';
                }

                cal.updateSchedule(schedule.id, schedule.calendarId, changes);
                refreshScheduleVisibility();
            },
            'beforeDeleteSchedule': function(e) {
                console.log('beforeDeleteSchedule', e);
                cal.deleteSchedule(e.schedule.id, e.schedule.calendarId);
            },
            'afterRenderSchedule': function(e) {
                var schedule = e.schedule;
                // var element = cal.getElement(schedule.id, schedule.calendarId);
                // console.log('afterRenderSchedule', element);
            },
            'clickTimezonesCollapseBtn': function(timezonesCollapsed) {
                console.log('timezonesCollapsed', timezonesCollapsed);

                if (timezonesCollapsed) {
                    cal.setTheme({
                        'week.daygridLeft.width': '77px',
                        'week.timegridLeft.width': '77px'
                    });
                } else {
                    cal.setTheme({
                        'week.daygridLeft.width': '60px',
                        'week.timegridLeft.width': '60px'
                    });
                }

                return true;
            }
        });


        /**
         * Get time template for time and all-day
         * @param {Schedule} schedule - schedule
         * @param {boolean} isAllDay - isAllDay or hasMultiDates
         * @returns {string}
         */
        function getTimeTemplate(schedule, isAllDay)
        {
            var html = [];
            var start = moment(schedule.start.toUTCString());
            if (!isAllDay) {
                html.push('<strong>' + start.format('HH:mm') + '</strong> ');
            }
            if (schedule.isPrivate) {
                html.push('<span class="calendar-font-icon ic-lock-b"></span>');
                html.push(' Private');
            } else {
                if (schedule.isReadOnly) {
                    html.push('<span class="calendar-font-icon ic-readonly-b"></span>');
                } else if (schedule.recurrenceRule) {
                    html.push('<span class="calendar-font-icon ic-repeat-b"></span>');
                } else if (schedule.attendees.length) {
                    html.push('<span class="calendar-font-icon ic-user-b"></span>');
                } else if (schedule.location) {
                    html.push('<span class="calendar-font-icon ic-location-b"></span>');
                }
                html.push(' ' + schedule.title);
            }

            return html.join('');
        }

        /**
         * A listener for click the menu
         * @param {Event} e - click event
         */
        function onClickMenu(e) {
            var target = $(e.target).closest('a[role="menuitem"]')[0];
            var action = getDataAction(target);
            var options = cal.getOptions();
            var viewName = '';

            console.log(target);
            console.log(action);
            switch (action) {
                case 'toggle-daily':
                    viewName = 'day';
                    break;
                case 'toggle-weekly':
                    viewName = 'week';
                    break;
                case 'toggle-monthly':
                    options.month.visibleWeeksCount = 0;
                    viewName = 'month';
                    break;
                case 'toggle-weeks2':
                    options.month.visibleWeeksCount = 2;
                    viewName = 'month';
                    break;
                case 'toggle-weeks3':
                    options.month.visibleWeeksCount = 3;
                    viewName = 'month';
                    break;
                case 'toggle-narrow-weekend':
                    options.month.narrowWeekend = !options.month.narrowWeekend;
                    options.week.narrowWeekend = !options.week.narrowWeekend;
                    viewName = cal.getViewName();

                    target.querySelector('input').checked = options.month.narrowWeekend;
                    break;
                case 'toggle-start-day-1':
                    options.month.startDayOfWeek = options.month.startDayOfWeek ? 0 : 1;
                    options.week.startDayOfWeek = options.week.startDayOfWeek ? 0 : 1;
                    viewName = cal.getViewName();

                    target.querySelector('input').checked = options.month.startDayOfWeek;
                    break;
                case 'toggle-workweek':
                    options.month.workweek = !options.month.workweek;
                    options.week.workweek = !options.week.workweek;
                    viewName = cal.getViewName();

                    target.querySelector('input').checked = !options.month.workweek;
                    break;
                default:
                    break;
            }

            cal.setOptions(options, true);
            cal.changeView(viewName, true);

            setDropdownCalendarType();
            setRenderRangeText();
            setSchedules();
        }

        function onClickNavi(e) {
            var action = getDataAction(e.target);

            switch (action) {
                case 'move-prev':
                    cal.prev();
                    break;
                case 'move-next':
                    cal.next();
                    break;
                case 'move-today':
                    cal.today();
                    break;
                default:
                    return;
            }

            setRenderRangeText();
            setSchedules();
        }

        function onNewSchedule() {
            var title = $('#new-schedule-title').val();
            var location = $('#new-schedule-location').val();
            var isAllDay = document.getElementById('new-schedule-allday').checked;
            var start = datePicker.getStartDate();
            var end = datePicker.getEndDate();
            var calendar = selectedCalendar ? selectedCalendar : ScheduleTypes[0];

            if (!title) {
                return;
            }

            cal.createSchedules([{
                id: String(chance.guid()),
                calendarId: calendar.id,
                title: title,
                isAllDay: isAllDay,
                start: start,
                end: end,
                category: isAllDay ? 'allday' : 'time',
                dueDateClass: '',
                color: calendar.color,
                bgColor: calendar.color,
                dragBgColor: calendar.color,
                borderColor: calendar.color,
                raw: {
                    location: location
                },
                state: 'Busy'
            }]);

            $('#modal-new-schedule').modal('hide');
        }

        function onChangeNewScheduleCalendar(e) {
            var target = $(e.target).closest('a[role="menuitem"]')[0];
            var calendarId = getDataAction(target);
            changeNewScheduleCalendar(calendarId);
        }

        function changeNewScheduleCalendar(calendarId) {
            var calendarNameElement = document.getElementById('calendarName');
            var calendar = findScheduleType(calendarId);
            var html = [];

            html.push('<span class="calendar-bar" style="background-color: ' + calendar.color + '; border-color:' + calendar.color + ';"></span>');
            html.push('<span class="calendar-name">' + calendar.name + '</span>');

            calendarNameElement.innerHTML = html.join('');

            selectedCalendar = calendar;
        }

        /**
         * 스케쥴 생성 이벤트 핸들러
         * @param event
         */
        function createNewSchedule(event)
        {
            openPopup(event);
        }

        function onChangeCalendars(e)
        {
            var calendarId = e.target.value;
            var checked = e.target.checked;
            var viewAll = document.querySelector('.lnb-calendars-item input');
            var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));
            var allCheckedCalendars = true;

            if (calendarId === 'all') {
                allCheckedCalendars = checked;

                calendarElements.forEach(function(input) {
                    var span = input.parentNode;
                    input.checked = checked;
                    span.style.backgroundColor = checked ? span.style.borderColor : 'transparent';
                });

                ScheduleTypes.forEach(function(calendar) {
                    calendar.checked = checked;
                });
            } else {
                findScheduleType(calendarId).checked = checked;

                allCheckedCalendars = calendarElements.every(function(input) {
                    return input.checked;
                });

                if (allCheckedCalendars) {
                    viewAll.checked = true;
                } else {
                    viewAll.checked = false;
                }
            }

            refreshScheduleVisibility();
        }

        function refreshScheduleVisibility() {
            var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));

            ScheduleTypes.forEach(function(calendar) {
                cal.toggleSchedules(calendar.id, !calendar.checked, false);
            });

            cal.render(true);

            calendarElements.forEach(function(input) {
                var span = input.nextElementSibling;
                span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
            });
        }

        function setDropdownCalendarType() {
            var calendarTypeName = document.getElementById('calendarTypeName');
            var calendarTypeIcon = document.getElementById('calendarTypeIcon');
            var options = cal.getOptions();
            var type = cal.getViewName();
            var iconClassName;

            if (type === 'day') {
                type = '오늘 일정';
                iconClassName = 'calendar-icon ic_view_day';
            } else if (type === 'week') {
                type = '주간 일정';
                iconClassName = 'calendar-icon ic_view_week';
            } else if (options.month.visibleWeeksCount === 2) {
                type = '2주간 일정';
                iconClassName = 'calendar-icon ic_view_week';
            } else if (options.month.visibleWeeksCount === 3) {
                type = '3주간 일정';
                iconClassName = 'calendar-icon ic_view_week';
            } else {
                type = '월간 일정';
                iconClassName = 'calendar-icon ic_view_month';
            }

            calendarTypeName.innerHTML = type;
            calendarTypeIcon.className = iconClassName;
        }

        function currentCalendarDate(format) {
            var currentDate = moment([cal.getDate().getFullYear(), cal.getDate().getMonth(), cal.getDate().getDate()]);

            return currentDate.format(format);
        }

        function setRenderRangeText() {
            var renderRange = document.getElementById('renderRange');
            var options = cal.getOptions();
            var viewName = cal.getViewName();

            var html = [];
            if (viewName === 'day') {
                html.push(currentCalendarDate('YYYY.MM.DD'));
            } else if (viewName === 'month' &&
                (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
                html.push(currentCalendarDate('YYYY.MM'));
            } else {
                html.push(moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD'));
                html.push(' ~ ');
                html.push(moment(cal.getDateRangeEnd().getTime()).format(' MM.DD'));
            }
            renderRange.innerHTML = html.join('');
        }

        /**
         * 스케쥴을 세팅한다.
         */
        function setSchedules()
        {
            cal.clear();

            var startDateTime = cal.getDateRangeStart();
            var startDate = moment(startDateTime.toDate()).format('YYYY-MM-DD');
            var calendarType;

            switch (cal.getViewName())
            {
                case "day"   : calendarType = "DAILY";
                case "week"  : calendarType = "WEEKLY";
                case "month" : calendarType = "MONTHLY";
            }

            $.ajax({
                url : "http://1hoon.iptime.org:8366/schedule-api/schedules",
                type : "GET",
                data : {"baseDate" : startDate, "calendarType" : calendarType}
            }).done((data, textStatus, jqXHR) => {
                // 스케쥴 초기화
                ScheduleList = [];
                // 스케쥴 생성
                for (var idx = 0; idx < data.length; idx++)
                {
                    var rawData = data[idx];
                    var schedule = new ScheduleInfo();

                    schedule.id = rawData.id;
                    schedule.calendarId = rawData.scheduleType;

                    schedule.title = rawData.name;
                    schedule.body = rawData.bigo;
                    schedule.isReadOnly = false;

                    schedule.isAllday = rawData.isAllDay;
                    if (schedule.isAllday)
                    {
                        schedule.category = 'allday';
                    }
                    else
                    {
                        schedule.category = 'time';
                    }
                    schedule.start = rawData.startDate;
                    schedule.end = rawData.endDate;

                    schedule.isPrivate = false;
                    schedule.location = "";
                    schedule.attendees = [];
                    schedule.recurrenceRule = "";
                    schedule.state = "";
                    schedule.color = "black";
                    schedule.bgColor = rawData.scheduleColor;
                    schedule.dragBgColor = rawData.scheduleColor;
                    schedule.borderColor = rawData.scheduleColor;

                    if (schedule.category === 'milestone') {
                        schedule.color = schedule.bgColor;
                        schedule.bgColor = 'transparent';
                        schedule.dragBgColor = 'transparent';
                        schedule.borderColor = 'transparent';
                    }

                    schedule.raw.customerId = rawData.customerId;
                    schedule.raw.userId = rawData.userId;
                    schedule.raw.payed = rawData.payed;
                    schedule.raw.tags = rawData.tags;

                    ScheduleList.push(schedule);
                }
                cal.createSchedules(ScheduleList);
                refreshScheduleVisibility();
            });
        }

        function setEventListener() {
            $('#menu-navi').on('click', onClickNavi);
            $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
            $('#lnb-calendars').on('change', onChangeCalendars);

            $('#btn-save-schedule').on('click', onNewSchedule);
            $('#btn-new-schedule').on('click', createNewSchedule);

            $('#dropdownMenu-calendars-list').on('click', onChangeNewScheduleCalendar);

            window.addEventListener('resize', resizeThrottled);
        }

        function getDataAction(target) {
            return target.dataset ? target.dataset.action : target.getAttribute('data-action');
        }

        resizeThrottled = tui.util.throttle(function() {
            cal.render();
        }, 50);

        window.cal = cal;

        setDropdownCalendarType();
        setRenderRangeText();
        setSchedules();
        setEventListener();

    });

})(window, tui.Calendar);