package com.ddng.adminuibootstrap.modules.main.form;

import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data @NoArgsConstructor
public class MainScheduleForm
{
    private String scheduleTypeName;
    private String customerName;
    private String customerTypeName;
    private String customerTelNo;
    private List<String> scheduleTags = new ArrayList<>();

    public MainScheduleForm(ScheduleDto scheduleDto, CustomerDto customerDto)
    {
        this.scheduleTypeName = scheduleDto.getScheduleTypeName();
        this.customerName = customerDto.getName();
        this.customerTypeName = customerDto.getTypeName();
        this.customerTelNo = customerDto.getTelNo();
        if (scheduleDto.getTags() != null)
        {
            this.scheduleTags = scheduleDto.getTags().stream().map(t -> t.getTitle()).collect(Collectors.toList());
        }
    }
}
