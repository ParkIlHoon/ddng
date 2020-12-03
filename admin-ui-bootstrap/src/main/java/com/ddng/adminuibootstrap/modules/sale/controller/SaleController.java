package com.ddng.adminuibootstrap.modules.sale.controller;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import com.ddng.adminuibootstrap.modules.sale.dto.AddCartDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CartDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import com.ddng.adminuibootstrap.modules.schedules.template.ScheduleTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sale")
@SessionAttributes({"cart"})
@RequiredArgsConstructor
public class SaleController
{
    private final ScheduleTemplate scheduleTemplate;
    private final ItemTemplate itemTemplate;

    @GetMapping
    public String main (Model model)
    {
        return "sale/main";
    }

    @PostMapping("/cart")
    public String addCart (@RequestBody AddCartDto dto,
                           @ModelAttribute CartDto cart)
    {
        System.out.println(dto.getScheduleId());

        ScheduleDto schedule = scheduleTemplate.getSchedule(dto.getScheduleId());

        List<ItemDto> items = new ArrayList<>();
        for (Long beautyId : dto.getBeauties())
        {
            ItemDto item = itemTemplate.getItem(beautyId);
            items.add(item);
        }

        System.out.println("");

        return "sale/main";
    }
}
