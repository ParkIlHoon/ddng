package com.ddng.adminuibootstrap.modules.common.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <h1>공통 RestTemplate 추상 클래스</h1>
 */
public abstract class AbstractTemplate
{
    public static final String CUSTOMER_API_PATH = "/customers";
    public static final String FAMILY_API_PATH = "/families";
    public static final String ITEM_API_PATH = "/items";
    public static final String SALE_API_PATH = "/sale";
    public static final String COUPON_API_PATH = "/coupons";
    public static final String SCHEDULE_API_PATH = "/schedules";
    public static final String TAG_API_PATH = "/tags";

    public RestTemplate restTemplate;
    public ServiceProperties serviceProperties;

    public AbstractTemplate(RestTemplate restTemplate, ServiceProperties serviceProperties)
    {
        this.restTemplate = restTemplate;
        this.serviceProperties = serviceProperties;
    }

    /**
     * ddng-customer-api URI 반환 메서드
     * @return
     */
    public final String getCustomerApiUri()
    {
        return this.serviceProperties.getCustomer();
    }

    /**
     * ddng-customer-api UriComponentsBuilder 반환 메서드
     * @return
     */
    public final UriComponentsBuilder getCustomerApiUriBuilder()
    {
        return UriComponentsBuilder.fromUriString(this.getCustomerApiUri());
    }

    /**
     * ddng-sale-api URI 반환 메서드
     * @return
     */
    public final String getSaleApiUri()
    {
        return this.serviceProperties.getSale();
    }

    /**
     * ddng-sale-api UriComponentsBuilder 반환 메서드
     * @return
     */
    public final UriComponentsBuilder getSaleApiUriBuilder()
    {
        return UriComponentsBuilder.fromUriString(this.getSaleApiUri());
    }

    /**
     * ddng-schedule-api URI 반환 메서드
     * @return
     */
    public final String getScheduleApiUri()
    {
        return this.serviceProperties.getSchedule();
    }

    /**
     * ddng-schedule-api UriComponentsBuilder 반환 메서드
     * @return
     */
    public final UriComponentsBuilder getScheduleApiUriBuilder()
    {
        return UriComponentsBuilder.fromUriString(this.getScheduleApiUri());
    }

    /**
     * ddng-statistic-api URI 반환 메서드
     * @return
     */
    public final String getStatisticApiUri()
    {
        return this.serviceProperties.getStatistic();
    }

    /**
     * ddng-statistic-api UriComponentsBuilder 반환 메서드
     * @return
     */
    public final UriComponentsBuilder getStatisticApiUriBuilder()
    {
        return UriComponentsBuilder.fromUriString(this.getStatisticApiUri());
    }

    /**
     * ddng-user-api URI 반환 메서드
     * @return
     */
    public final String getUserApiUri()
    {
        return this.serviceProperties.getUser();
    }

    /**
     * ddng-user-api UriComponentsBuilder 반환 메서드
     * @return
     */
    public final UriComponentsBuilder getUserApiUriBuilder()
    {
        return UriComponentsBuilder.fromUriString(this.getUserApiUri());
    }
}
