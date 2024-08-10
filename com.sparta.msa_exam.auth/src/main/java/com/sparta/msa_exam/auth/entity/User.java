package com.sparta.msa_exam.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Entity
@Table(name = "_user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String loginId;

    @Column
    private String password;

    public static User CreateUser(String loginId, String password) {
        User user = new User();
        user.loginId = loginId;
        user.password = password;
        return user;
    }
}
