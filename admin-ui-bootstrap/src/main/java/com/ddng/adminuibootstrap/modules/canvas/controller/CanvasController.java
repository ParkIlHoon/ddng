package com.ddng.adminuibootstrap.modules.canvas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/canvas")
public class CanvasController
{
    @RequestMapping("/main")
    public String mainForm (Model model)
    {
        return "canvas/main";
    }
}
