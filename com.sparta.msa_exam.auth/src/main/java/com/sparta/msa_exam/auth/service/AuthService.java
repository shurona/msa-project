package com.sparta.msa_exam.auth.service;

import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long signupUser(String loginId, String password) {
        Optional<User> userByLoginId = authRepository.findByLoginId(loginId);
        if (userByLoginId.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User user = User.CreateUser(loginId, passwordEncoder.encode(password));
        authRepository.save(user);
        return user.getId();
    }


}
