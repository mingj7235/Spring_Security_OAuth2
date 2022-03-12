package com.joshua.spring_security_jwt.security_01.config.auth;

// 시큐리티가 /login 주소를 낚아채서 로그인을 진행 시킨다.
//로그인을 진행이 완료가 되면 시큐린티 session 에 넣는다. (session을 만들어 준다.) (SecurityContextHolder 라는 키값을 담아서 세션 정보에 저장한다.)
// 오브젝트 타입=> Authentication 객체만이 session에 들어갈 수 있다.
//Authentication 안에 User정보가 있어야 한다.
//User 오브텍트의 타입 => UserDetails타입 객체

// Security Session => Authentication 을 넣도록 정해져있다. => UserDetails 타입으로 넣어야한다. 정해놓음!!

import com.joshua.spring_security_jwt.security_01.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user; //콤포지션을 해야한다.

    public PrincipalDetails(final User user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳 !
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 우리사이트에서 1년동안 회원이 로그인을 안하면 휴면 계쩡으로 하기로 했다면
        // 현재시간 - 로그인 시간 (user 필드에 로그인 시간 필드를 넣어준다) 해서 비교해주면 된다.
        // 이곳에서 Enable을 false로 리턴해주면된다.

        return true;
    }

}
