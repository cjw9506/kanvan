package com.kanvan.auth.service;

import com.kanvan.auth.dto.UserSignupRequest;
import com.kanvan.user.domain.User;
import com.kanvan.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    @Transactional
    public void signup(UserSignupRequest request) {

        //todo 계정 중복 검사

        User user = User.builder()
                .account(request.getAccount())
                .password(request.getPassword()) //todo 암호화
                .username(request.getUsername())
                .build();

        userRepository.save(user);
    }
}
