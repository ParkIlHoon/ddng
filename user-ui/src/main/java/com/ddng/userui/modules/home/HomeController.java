package com.ddng.userui.modules.home;

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

        Card card1 = Card.builder()
                .title("푸들 썸머 커트")
                .thumbnailPath("/images/home/card/card-1.jpg")
                .build();
        cardList.add(card1);

        Card card2 = Card.builder()
                .title("시츄 풀 코트")
                .thumbnailPath("/images/home/card/card-2.jpg")
                .build();
        cardList.add(card2);

        Card card3 = Card.builder()
                .title("말티즈 양미용")
                .thumbnailPath("/images/home/card/card-3.jpg")
                .build();
        cardList.add(card3);

        model.addAttribute("cardList", cardList);

        return "home/main";
    }
}
