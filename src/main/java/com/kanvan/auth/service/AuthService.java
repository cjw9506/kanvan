package com.kanvan.auth.service;

import com.kanvan.auth.dto.UserLoginRequest;
import com.kanvan.auth.dto.UserLoginResponse;
import com.kanvan.auth.dto.UserSignupRequest;
import com.kanvan.auth.jwt.JwtUtils;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.user.domain.User;
import com.kanvan.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    @Transactional
    public void signup(UserSignupRequest request) {

        if (userRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }

        User user = User.builder()
                .account(request.getAccount())
                .password(request.getPassword()) //todo 암호화
                .username(request.getUsername())
                .build();

        userRepository.save(user);
    }

    public UserLoginResponse login(UserLoginRequest request) {

        userRepository.findByAccount(request.getAccount())
                .filter(user -> {
                    if (request.getPassword().equals(user.getPassword())) {
                        return true;
                    } else {
                        throw new CustomException(ErrorCode.USER_PASSWORD_MISMATCH);
                    }
                })
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserLoginResponse.builder()
                .bearer(jwtUtils.generateAccessToken(request.getAccount()))
                .build();
    }
}
