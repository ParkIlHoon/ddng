package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.statistic.CalculateDto.ByItemType;
import com.ddng.adminuibootstrap.modules.common.dto.statistic.CalculateDto.ByPayment;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatisticClientFallback implements StatisticClient{

    @Override
    public List<ByItemType> getCalculateByItemType(String baseDate) {
        return Collections.emptyList();
    }

    @Override
    public List<ByPayment> getCalculateByPayment(String baseDate) {
        return Collections.emptyList();
    }
}
