package cse.jaejin.running.security;

import cse.jaejin.running.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 예: ROLE_USER 또는 ROLE_ADMIN
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 암호화된 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // 로그인 시 사용할 유저 ID
    }

    // 아래는 항상 true로 반환 (계정 만료, 잠김 등 사용하지 않을 경우)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public User getUser() {
        return user;
    }
}
