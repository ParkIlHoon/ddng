package com.ddng.adminuibootstrap.modules.statistic.controller;

import com.ddng.adminuibootstrap.modules.common.clients.StatisticClient;
import com.ddng.adminuibootstrap.modules.common.dto.statistic.CalculateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

/**
 * <h1>통계 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticClient statisticClient;

    /**
     * 정산 메뉴 폼 요청
     *
     * @param baseDate 정산 기준일자
     */
    @GetMapping("/calculate")
    public String calculateForm(String baseDate, Model model) {
        String baseDateParam = StringUtils.isEmpty(baseDate) ? LocalDate.now().toString() : baseDate;

        List<CalculateDto.ByItemType> byItemType = statisticClient.getCalculateByItemType(baseDateParam);
        List<CalculateDto.ByPayment> byPayment = statisticClient.getCalculateByPayment(baseDateParam);

        long byItemTypeSumAmount = byItemType.stream().mapToLong(CalculateDto.ByItemType::getAmount).sum();
        long byItemTypeSumCount = byItemType.stream().mapToLong(CalculateDto.ByItemType::getCount).sum();
        long byPaymentSumAmount = byPayment.stream().mapToLong(CalculateDto.ByPayment::getAmount).sum();
        long byPaymentSumCount = byPayment.stream().mapToLong(CalculateDto.ByPayment::getCount).sum();

        model.addAttribute("baseDate", baseDateParam);
        model.addAttribute("byItemType", byItemType);
        model.addAttribute("byItemTypeSumAmount", byItemTypeSumAmount);
        model.addAttribute("byItemTypeSumCount", byItemTypeSumCount);
        model.addAttribute("byPayment", byPayment);
        model.addAttribute("byPaymentSumAmount", byPaymentSumAmount);
        model.addAttribute("byPaymentSumCount", byPaymentSumCount);

        return "statistic/calculate/main";
    }

}
