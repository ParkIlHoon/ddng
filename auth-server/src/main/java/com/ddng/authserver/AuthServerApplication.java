package com.ddng.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * <h1>OAuth2 인증서버</h1>
 *
 * 세부 설정은 {@link com.ddng.authserver.config.AuthConfig} 에서 지정함
 * @author ParkIlHoon
 * @version 1.0
 */
@EnableAuthorizationServer  // OAuth2 인증서버를 활성화시켜주는 어노테이션
@SpringBootApplication
public class AuthServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
