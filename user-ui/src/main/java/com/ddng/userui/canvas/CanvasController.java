package com.ddng.userui.canvas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CanvasController
{
    @GetMapping("/canvas")
    public String main (Model model)
    {
        return "canvas/main";
    }
}
