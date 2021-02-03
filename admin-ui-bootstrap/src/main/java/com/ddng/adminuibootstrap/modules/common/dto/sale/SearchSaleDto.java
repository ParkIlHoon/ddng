package com.ddng.adminuibootstrap.modules.common.dto.sale;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data @Builder @ToString
public class SearchSaleDto
{
    private boolean onlyToday;    // 오늘꺼만 조회할지 여부
    private LocalDateTime salePeriodStart;
    private LocalDateTime salePeriodEnd;
}
