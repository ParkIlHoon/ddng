package com.ddng.userui.canvas;

import com.ddng.userui.home.Card;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CanvasController
{
    @GetMapping("/canvas")
    public String main (Model model)
    {
        List<Card> cardList = new ArrayList<>();

        Card bolt = Card.builder()
                        .title("자연스러운 기장")
                        .content("너무 짧지 않으면서 깔끔한, 자연스러운 기장으로 커트")
                        .thumbnailPath("/images/canvas/bolt.jpeg")
                        .build();
        Card rani = Card.builder()
                        .title("시츄 가위 컷")
                        .content("시츄는 사실 이렇게 생겼습니다.")
                        .thumbnailPath("/images/canvas/rani.jpeg")
                        .build();
        cardList.add(bolt);
        cardList.add(rani);

        for(int idx = 1; idx < 18; idx++)
        {
            Card card = Card.builder()
                    .title("미용 이름" + idx)
                    .content("미용 설명" + idx)
                    .thumbnailPath("/images/beauty/work-" + idx + ".jpg")
                    .build();
            cardList.add(card);
        }

        model.addAttribute("cardList", cardList);
        return "canvas/main";
    }
}
