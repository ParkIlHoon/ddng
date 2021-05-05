package com.ddng.userui.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>service properties 객체</h1>
 *
 * application.properties 의 service 설정을 객체화 하는 클래스
 *
 */
@Getter @Setter
@ConfigurationProperties("service")
public class ServiceProperties
{
    /**
     * 첨부파일 Root Path
     */
    private String file;
}
