package com.joshua.spring_security_jwt.security_01.config.oauth;

import com.joshua.spring_security_jwt.security_01.config.auth.PrincipalDetails;
import com.joshua.spring_security_jwt.security_01.model.User;
import com.joshua.spring_security_jwt.security_01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final UserRepository repository;

    // google 로부터 받은 userRequest 데이터에 대한 후처리되는 함수시
    // 메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //RegistrationId 로 어떤 Oauth로 로그인했는지 확인 가능 (google인지 naver인지 ..)
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());

        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // google 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴 받음 (oauth2Client 라이브러리가 받음) ->
        // AccessToken 요청 -> 여기까지가 userRequest 정보다.
        // userRequest 정보 -> 회원 프로필을 받아야 한다. 이때 사용되는 함수가 loadUser 함수다. 이 함수를 통해 회원 프로필을 받을 수 있다.
        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        //회원 가입을 강제로 진행 즉, 구글로부터 받은 userRequest로 후처리 통해서 강제 회원가입
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId; // google_109231423123
        String password = bCryptPasswordEncoder.encode("겟인데어"); //oauth로 가입하는거라 의미없음
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = repository.findByUsername(username);

        if (userEntity == null) {
            System.out.println("구글 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            repository.save(userEntity);
        } else {
            System.out.println("구글 로그인을 이미 한적이 잇습니다. 당신은 자동 회원 가입이 되어있습니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }

}
