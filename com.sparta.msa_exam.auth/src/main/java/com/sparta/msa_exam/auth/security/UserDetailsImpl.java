package com.sparta.msa_exam.auth.security;

import com.sparta.msa_exam.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return passwordEncoder.encode(user.getLoginId());
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }
}
