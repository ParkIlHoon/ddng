package com.ddng.adminuibootstrap.modules.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item")
public class ItemController
{
    @GetMapping("/search")
    public String mainForm (Model model)
    {
        return "item/main";
    }

    @GetMapping("/new")
    public String newItemForm (Model model)
    {
        return "item/new-item";
    }

    @GetMapping("/barcode")
    public String barcodeForm (Model model)
    {
        return "item/barcode";
    }
}
