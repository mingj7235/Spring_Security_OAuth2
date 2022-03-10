package com.joshua.spring_security_jwt.security_01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // Rest와 달리 view를 리턴하겠다는 것인데 머스테치 설정이잇으므로 자동으로 index view를 반환함
public class IndexController {

    @GetMapping ({"", "/"})
    public String index () {
        // mustache 기본 폴더 : src/main/resources/
        // 뷰 리졸버 설정 : templates (prefix) / .mustache (suffix) 로 잡는다. 단, mustache 를 의존성받아오면 따로설정 노필요
        return "index"; // src/main/resources/templates/index.mustache 를 찾게 되므로 config 설정을 해줘야한다. (재설정해줘야함)
    }

    @GetMapping ("/user")
    public String user () {
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

    @GetMapping ("/login")
    public String login () {
        return "loginForm";
    }

    @GetMapping ("/join")
    public String join () {
        return "join";
    }

    @GetMapping ("/joinProc")
    public @ResponseBody String joinProc () {
        return "회원가입 완료됨!";
    }
}
