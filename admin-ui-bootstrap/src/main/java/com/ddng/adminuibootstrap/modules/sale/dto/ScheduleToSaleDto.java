package com.ddng.adminuibootstrap.modules.sale.dto;

import com.ddng.adminuibootstrap.modules.customer.dto.CustomerDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ScheduleToSaleDto
{
    private Long id;
    private String name;
    private String scheduleType;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    private boolean allDay;
    private Long customerId;
    private Long userId;
    private String bigo;
    private boolean payed;

    private String scheduleTypeName;
    private String scheduleColor;

    private String customerName;
    private String customerTypeName;
    private String customerTelNo;
    private String customerProfileImg;

    public ScheduleToSaleDto(ScheduleDto scheduleDto, CustomerDto customerDto)
    {
        this.id = scheduleDto.getId();
        this.name = scheduleDto.getName();
        this.scheduleType = scheduleDto.getScheduleType();
        this.startDate = scheduleDto.getStartDate();
        this.endDate = scheduleDto.getEndDate();
        this.allDay = scheduleDto.isAllDay();
        this.customerId = scheduleDto.getCustomerId();
        this.userId = scheduleDto.getUserId();
        this.bigo = scheduleDto.getBigo();
        this.payed = scheduleDto.isPayed();

        this.scheduleTypeName = scheduleDto.getScheduleTypeName();
        this.scheduleColor = scheduleDto.getScheduleColor();

        this.customerName = customerDto.getName();
        this.customerTypeName = customerDto.getTypeName();
        this.customerTelNo = customerDto.getTelNo();
        this.customerProfileImg = customerDto.getProfileImg();
    }
}
