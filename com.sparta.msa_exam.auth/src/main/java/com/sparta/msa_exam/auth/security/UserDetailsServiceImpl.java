package com.sparta.msa_exam.auth.security;

import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.repository.AuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthRepository authRepository;

    public UserDetailsServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        System.out.println(loginId);
        User user = authRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("없는 유저 입니다."));

        return new UserDetailsImpl(user);
    }
}
