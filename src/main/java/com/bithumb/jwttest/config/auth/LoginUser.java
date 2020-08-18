package com.bithumb.jwttest.config.auth;

import com.bithumb.jwttest.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
public class LoginUser extends org.springframework.security.core.userdetails.User {
    private User member;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public LoginUser(String username, String password, boolean enabled,
                     boolean accountNonExpired, boolean credentialsNonExpired,
                     boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                     User member) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.member = member;
    }

    public LoginUser(User member) {
        this(member.getEmail(), member.getPassword(), Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())));
        this.member = member;
    }
}
