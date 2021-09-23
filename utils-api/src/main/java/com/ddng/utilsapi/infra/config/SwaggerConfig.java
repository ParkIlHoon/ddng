package com.ddng.utilsapi.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 설정 클래스
 */
@Configuration
@EnableSwagger2
@EnableHypermediaSupport(type=EnableHypermediaSupport.HypermediaType.HAL)
public class SwaggerConfig
{
    private static final String NAME = "ParkIlHoon";
    private static final String URL = "https://github.com/ParkIlHoon/ddng";
    private static final String EMAIL = "chiwoo2074@gmail.com";

    @Bean
    public Docket docket ()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                    .useDefaultResponseMessages(false)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.ddng.utilsapi.modules"))
                    .paths(PathSelectors.ant("/**"))
                    .build()
                    .apiInfo(apiInfo());
    }

    @Bean
    public LinkDiscoverers discovers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

    /**
     * API 기본 정보 명세
     * @return 명세한 API 기본 정보
     */
    private ApiInfo apiInfo ()
    {
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("ddng-utils-api")
                .contact(new Contact(NAME, URL, EMAIL))
                .description("똥강아지 프로젝트 유틸 관련 API")
                .version("1.0");

        return builder.build();
    }
}
