package com.song.security1.config.oauth;

import com.song.security1.config.auth.PrincipalDetails;
import com.song.security1.model.User;
import com.song.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest데이터에 대한 후처리가 되는 함수
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : "+ userRequest.getClientRegistration()); // registerationId로 어떤 OAuth로 로그인했는지 확인가능.
        System.out.println("getAccessToken : "+ userRequest.getAccessToken());

        OAuth2User oauth2user =super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필을 받아준다.
        System.out.println("getAttributes : "+ oauth2user.getAttributes());

        // 회원가입을 강제로 진행해볼 예정
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oauth2user.getAttribute("sub");
        String email = oauth2user.getAttribute("email");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            System.out.println("구글 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("구글 로그인을 이미 한 적이 있습니다. 당신은 자동회원가입 되어 있습니다.");
        }

       return new PrincipalDetails(userEntity, oauth2user.getAttributes());
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails: "+principalDetails.getUser());
        return "user";
    }

}