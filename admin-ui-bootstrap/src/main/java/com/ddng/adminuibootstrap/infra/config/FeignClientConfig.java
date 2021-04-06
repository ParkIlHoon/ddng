package com.ddng.adminuibootstrap.infra.config;

import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

/**
 * <h1>Spring Cloud Feign 설정 클래스</h1>
 */
@Configuration
public class FeignClientConfig
{
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * Get 요청에서 RequestParam으로 LocalTime을 보낼 때 ISO Format cjfl
     * @return
     */
    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegister ()
    {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }

    @Bean
    public Encoder feignEncoder()
    {
        return new PageableQueryEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public Module customModule()
    {
        SimpleModule module = new SimpleModule("simple-feign", new Version(0, 0, 1, "SNAPSHOT", "com.parfait", "simple-feign"));
        module.setMixInAnnotation(Page.class, FeignPageImpl.class);
        return module;
    }

    @JsonDeserialize(as = FeignPageImpl.class)
    private interface PageMixIn {}
}
