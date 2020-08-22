package com.ddng.userui.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController
{
    @GetMapping("/")
    public String main(Model model)
    {
        List<Card> cardList = new ArrayList<>();

        for(int idx = 1; idx < 7; idx++)
        {
            Card card = Card.builder()
                                .title("타이틀" + idx)
                                .content("컨텐츠" + idx)
                                .thumbnailPath("/images/work-" + idx + ".jpg")
                            .build();
            cardList.add(card);
        }

        model.addAttribute("cardList", cardList);

        return "home/main";
    }
}
