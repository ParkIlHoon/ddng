package com.ddng.adminuibootstrap.modules.statistic.controller;

import com.ddng.adminuibootstrap.modules.statistic.dto.CalculateDto;
import com.ddng.adminuibootstrap.modules.statistic.template.StatisticTemplate;
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
public class StatisticController
{
    private final StatisticTemplate statisticTemplate;

    /**
     * 정산 메뉴 폼 요청
     * @param baseDate 정산 기준일자
     * @param model
     * @return
     */
    @GetMapping("/calculate")
    public String calculateForm(String baseDate, Model model)
    {
        String baseDateParam = StringUtils.isEmpty(baseDate)? LocalDate.now().toString() : baseDate;

        List<CalculateDto.ByItemType> byItemType = statisticTemplate.getCalculateByItemType(baseDateParam);
        List<CalculateDto.ByPayment> byPayment = statisticTemplate.getCalculateByPayment(baseDateParam);

        int byItemTypeSumAmount = byItemType.stream().mapToInt(CalculateDto.ByItemType::getAmount).sum();
        int byItemTypeSumCount = byItemType.stream().mapToInt(CalculateDto.ByItemType::getCount).sum();
        int byPaymentSumAmount = byPayment.stream().mapToInt(CalculateDto.ByPayment::getAmount).sum();
        int byPaymentSumCount = byPayment.stream().mapToInt(CalculateDto.ByPayment::getCount).sum();

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
