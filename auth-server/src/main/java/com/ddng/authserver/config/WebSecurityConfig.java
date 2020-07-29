package com.ddng.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <h1>웹 Security 설정</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 * @see com.ddng.authserver.config.AuthConfig
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    /**
     * AuthenticationManager Bean 노출을 위한 설정
     * 노출된 Bean은 {@link com.ddng.authserver.config.AuthConfig} 에서 사용한다
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        // H2 Console 접근을 위한 임시 처리
        http.headers().frameOptions().disable()
            .and()
            .anonymous();
    }
}
