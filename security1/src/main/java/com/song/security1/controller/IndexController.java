package com.song.security1.controller;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.song.security1.config.auth.PrincipalDetails;
import com.song.security1.model.User;
import com.song.security1.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal UserDetails userDetails){ //DI(의존성 주입)
        System.out.println("/test/login ==========================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : "+ principalDetails.getUser());

        System.out.println("userDetails:"+userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth){ //DI(의존성 주입)
        System.out.println("/test/oauth/login ==========================");
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oauth2User :"+ oauth.getAttributes());
        System.out.println("authentication : "+ oauth2User.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }
    //localhost:8080
    //localhost:8080
    @GetMapping({"", "/"})
    public @ResponseBody String index() {
        //mustache 기본 폴더 src/main/resources/
        //뷰 리졸버 설정 : templates (prefix), .mustache (suffix)
        return "index"; // src/main/resources/templates/index.mustache
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
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
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 여러개를 걸고 싶다면
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 정보";
    }
}
