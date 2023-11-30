package com.kanvan.auth.service;

import com.kanvan.auth.dto.UserLoginRequest;
import com.kanvan.auth.dto.AuthenticationResponse;
import com.kanvan.auth.dto.UserSignupRequest;
import com.kanvan.auth.jwt.JwtService;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.user.domain.Role;
import com.kanvan.user.domain.User;
import com.kanvan.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(UserSignupRequest request) {

        if (userRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }

        User user = User.builder()
                .account(request.getAccount())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user); //회원가입과 동시에 로그인
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getAccount(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByAccount(request.getAccount()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
