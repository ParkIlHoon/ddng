package com.ddng.userui.home;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Card
{
    private String title;
    private String content;
    private String thumbnailPath;
}
