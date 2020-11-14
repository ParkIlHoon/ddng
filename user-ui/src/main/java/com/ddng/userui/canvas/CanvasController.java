package com.ddng.userui.canvas;

import com.ddng.userui.home.Card;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CanvasController
{
    private List<Card> cardList = new ArrayList<>();

    public CanvasController()
    {
        for(int idx = 1; idx < 26; idx++)
        {
            Card card = Card.builder()
                    .id((long) idx)
                    .title("미용 이름" + idx)
                    .content("미용 설명" + idx)
                    .thumbnailPath("/images/canvas/canvas-" + idx + ".jpg")
                    .build();
            cardList.add(card);
        }
    }

    @GetMapping("/canvas")
    public String main (Model model)
    {
        model.addAttribute("cardList", cardList);
        return "canvas/main";
    }

    @GetMapping("/canvas/{id}")
    public String detail (@PathVariable("id") Long id, Model model)
    {
        Card card = cardList.get(Integer.parseInt(id.toString()) - 1);
        model.addAttribute("card", card);
        return "canvas/card-detail";
    }
}
