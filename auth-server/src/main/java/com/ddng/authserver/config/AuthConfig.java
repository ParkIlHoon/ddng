package com.ddng.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * <h1>OAuth2 세부 설정</h1>
 *
 * 클라이언트 정보 저장 테이블과 사용 데이터베이스, 토큰 생성 방식을 설정한다.
 * @author ParkIlHoon
 * @version 1.0
 * @see com.ddng.authserver.config.WebSecurityConfig
 */
@Configuration
@SpringBootApplication
public class AuthConfig extends AuthorizationServerConfigurerAdapter
{
    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * AuthenticationManager를 Autowired하려면
     * WebSecurityConfigurerAdapter를 상속하는 Config 클래스를 작성하고
     * WebSecurityConfigurerAdapter의 authenticationManagerBean을 오버라이드, Bean 생성 해야함
     * {@link com.ddng.authserver.config.WebSecurityConfig} 에서 해당 내용 설정함
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ResourceServerProperties resourceServerProperties;

    /**
     * oauth_client_details 테이블에 등록된 사용자로 조회
     *
     * 서버 시작 시, resources/schema.sql 파일로 스키마를 생성해준다.
     * @param clients
     * @throws Exception
     * @see <a href="https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql">Spring 제공 기본 DB Schema</a>
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 인증 과정 endpoint 설정
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        super.configure(endpoints);
        endpoints.accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    /**
     * H2 데이터베이스 이용 oauth client 정보등록 설정을 위한 Bean
     * @param dataSource
     * @return
     */
    @Bean
    @Primary
    public JdbcClientDetailsService JdbcClientDetailsService(DataSource dataSource)
    {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * JWT key-value 방식 설정을 위한 Bean
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter()
    {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(resourceServerProperties.getJwt().getKeyValue());

        return accessTokenConverter;
    }
}
