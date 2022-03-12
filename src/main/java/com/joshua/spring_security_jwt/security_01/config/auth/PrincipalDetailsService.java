package com.joshua.spring_security_jwt.security_01.config.auth;

import com.joshua.spring_security_jwt.security_01.model.User;
import com.joshua.spring_security_jwt.security_01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 지켜야할 Rule
// Security 설정에서 loginProcessingUrl('login')
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 Ioc 되어있는 loadUserByUsername 함수가 실행 된다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository repository;

    // Security Session => Authentication => UserDetails
    // 메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // username 은 loginForm.html 에 있는 form 태그의 username과 매칭이 되는 것이다.
        // parameter값을 바꾸고싶다면 SecurityConfig의 login옵션에서 usernameParameter 설정을 변경해줘야한다.

        User userEntity = repository.findByUsername(username);

        if(userEntity != null)
            return new PrincipalDetails(userEntity);

        return null;
    }

}
