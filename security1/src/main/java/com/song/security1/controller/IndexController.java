package com.song.security1.controller;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.song.security1.model.User;
import com.song.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //View를 리턴하겠다.
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //localhost:8080
    //localhost:8080
    @GetMapping({"", "/"})
    public @ResponseBody String index() {
        //mustache 기본 폴더 src/main/resources/
        //뷰 리졸버 설정 : templates (prefix), .mustache (suffix)
        return "index"; // src/main/resources/templates/index.mustache
    }
    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 스프링 시큐리티가 해당 주소를 낚아 챈다. -SecurityConfig 파일 생성 후 작동 안함.
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm") // 회원가입 폼으로 이동
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join") //실제로 회원가입 수행
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER"); //ROLE_USER 라고 해서 강제로 넣어준다.
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword); //패스워드 암호화
        user.setPassword(encPassword); // 암호화된 패스워드 넣어줌.

        userRepository.save(user); //회원가입 잘됨. 비밀번호 1234 => 시큐리티로 로그인 할 수 없음 => 해쉬화되지 않았기 때문에..
        return "redirect:/loginForm";
    }
}
