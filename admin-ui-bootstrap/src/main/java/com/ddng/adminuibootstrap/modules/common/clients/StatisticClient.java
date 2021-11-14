package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.statistic.CalculateDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ddng-statistic-api", fallback = StatisticClientFallback.class)
public interface StatisticClient {

    String CALCULATE_API = "/calculate";

    /**
     * 상품 종류별 정산 데이터를 조회한다.
     * @param baseDate 정산 기준일자
     */
    @GetMapping(CALCULATE_API + "/itemType")
    List<CalculateDto.ByItemType> getCalculateByItemType(@RequestParam("baseDate") String baseDate);

    /**
     * 결제 수단별 정산 데이터를 조회한다.
     * @param baseDate 정산 기준일자
     */
    @GetMapping(CALCULATE_API + "/payment")
    List<CalculateDto.ByPayment> getCalculateByPayment(@RequestParam("baseDate") String baseDate);
}
