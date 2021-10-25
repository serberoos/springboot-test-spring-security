package com.song.security1.config.auth;

// 시큐리티가 /login을 낚아채서 로그인을 진행 시킨다.
// 로그인을 진행 완료가 되면 시큐리티 session을 만들어 줍니다.(security ContextHolder)
// 오브젝트 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 하마.
// USer 오브젝트 타입 => UserDetails 타입 객체

//Security Session=>Authentication=> UserDetails(PrincipalDetails)  | 정해져 있음.

import com.song.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 콤포지션

    public PrincipalDetails(User user){
        this.user=user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() { //String 데이터 Role을 이런식으로 받는다.
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
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
    public boolean isAccountNonExpired() { //만료된 계정이니?
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //이 계정 잠겼니?
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //계정의 비밀번호가 1년이 지났니?
        return true;
    }

    @Override
    public boolean isEnabled() { // 계정이 활성화 되어 있니?

        //우리 사이트!! 1년동안 회원이 로그인을 안하면 휴면 계정으로 하게됨
        // 현재 시간 - 로긴시간 = 1년 초과 시 false
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
