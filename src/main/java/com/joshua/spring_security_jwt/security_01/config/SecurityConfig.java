package com.joshua.spring_security_jwt.security_01.config;

import com.joshua.spring_security_jwt.security_01.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 1. 코드 받기 (인증이 되었음 즉, 구글에 정상적인 로그인)
 * 2. 엑세스 토큰 (사용자 정보에 접근할 권한)
 * 3. 사용자 프로필 정보를 가져옴
 * 4-1. 정보를 토대로 회원가입을 자동으로 진행
 * 4-2. 이메일, 전화번호, 이름, 아이디 정도만 구글이 가지고 있을 경우 즉, 더 많은 정보가 내 서비스에서 필요한 경우에는
 *      추가정보를 더 해서 회원가입을 진행시킨다.
  */
//

/**
 * 스프링 시큐리티는 스프링시큐리티만의 session이 따로 있다.
 *   - Authentication 객체가 여기에 들어와야한다. 이것만!!!
 *   - 이걸 DI 해서 사용가능하다.
 *   - Authentication 에 2가지 타입이 들어갈 수 잇다.
 *      - UserDetails : 일반적인 서비스 서버에서 로그인할 때
 *      - OAuth2User : 구글, 페이스북 등의 OAuth 로그인을 할 때
 *      => 두가지를 implement 받은 class 를 구현하여 로그인 처리한다.
 */

@Configuration
@EnableWebSecurity // Spring Security Filter 가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity (securedEnabled = true, prePostEnabled = true)
                        // securedEnabled : secured 어노테이션 활성화 : @Secured를 사용한 컨트롤러를 제어할 수 있다.
                        // prePostEnabled : preAuthorize, postAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    // 해당 메서드의 리턴되는 오브텍트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated() // 회원 인증만 되면 들어갈 수 있는 주소 !
                    .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
//                .usernameParameter("username2") // parameter 값을 바꾸고싶다면
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행한다.
                .defaultSuccessUrl("/") // 로그인 성공 시 메인으로 감
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후처리가 필요하다. Tip. 코드를 만드는 것이아니라 엑세스토큰 + 사용자프로필정보를 받는다.
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService) // 후처리를 여기서 한다.

        ;
    }

}
