package com.joshua.spring_security_jwt.security_01.controller;

import com.joshua.spring_security_jwt.security_01.model.User;
import com.joshua.spring_security_jwt.security_01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // Rest와 달리 view를 리턴하겠다는 것인데 머스테치 설정이잇으므로 자동으로 index view를 반환함
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository repository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping ({"", "/"})
    public String index () {
        // mustache 기본 폴더 : src/main/resources/
        // 뷰 리졸버 설정 : templates (prefix) / .mustache (suffix) 로 잡는다. 단, mustache 를 의존성받아오면 따로설정 노필요
        return "index"; // src/main/resources/templates/index.mustache 를 찾게 되므로 config 설정을 해줘야한다. (재설정해줘야함)
    }

    @GetMapping ("/user")
    public @ResponseBody String user () {
        return "user";
    }

    @GetMapping ("/admin")
    public String admin () {
        return "admin";
    }

    @GetMapping ("/manager")
    public String manager () {
        return "manager";
    }

    @GetMapping ("/loginForm")
    public String loginForm () {
        return "loginForm";
    }

    @GetMapping ("/joinForm")
    public String joinForm () {
        return "joinForm";
    }

    @PostMapping ("/join")
    public String join (User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        repository.save(user); //회원가입 잘되지만 비밀번호 => security로 로그인 못함. 이유는 패스워드가 암호화가 안되었기 때문이다. 인코디해줘야한다.
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // SecurityConfig 에서 EnableGlobalMethodSecurity 설정해줘야한다.
    @GetMapping ("/info")
    public @ResponseBody String info () {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data() 가 실행되기 직전에 실행된다.
//    @PostAuthorize() // data()가 실행된 후에
    @GetMapping ("/data")
    public @ResponseBody String data () {
        return "data";
    }



}
