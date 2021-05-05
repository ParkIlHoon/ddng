package com.ddng.userui.modules.home;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Card
{
    private Long id;
    private String title;
    private String content;
    private String thumbnailPath;
}
