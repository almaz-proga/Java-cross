package com.example.cross_project.controller;


import com.example.cross_project.dto.LoginRequest;
import com.example.cross_project.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;

import com.example.cross_project.dto.LoginResponse;
import com.example.cross_project.dto.UserLogged;
import com.example.cross_project.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    @Operation(summary = "Аутентификация пользователя", description = "Выполняет вход пользователя по логину и паролю. Может использовать существующие access и refresh токены из cookies")
    public ResponseEntity<LoginResponse> login(
        @CookieValue(name="access-token", required = false) String access,
        @CookieValue(name="refresh-token", required = false) String refresh,
        @RequestBody LoginRequest loginRequest) {
            return authenticationService.login(loginRequest, access, refresh);
        }
    
    @PostMapping("/refresh")
    @Operation(summary = "Обновить токен доступа", description = "Обновляет access-токен с использованием refresh-токена из cookies")
    public ResponseEntity<LoginResponse> refresh(
        @CookieValue(name = "refresh-token", required = false) String refresh) {
            return authenticationService.refresh(refresh);
        }
    
    @PostMapping("/logout")
    @Operation(summary = "Выход пользователя", description = "Инвалидирует access-токен и завершает сессию пользователя")
    public ResponseEntity<LoginResponse> logout(
        @CookieValue(name = "access-token", required = false) String access) {
            return authenticationService.logout(access);
        }
    
    @GetMapping("/info")
    @Operation(summary = "Получить информацию о текущем пользователе", description = "Возвращает данные авторизованного пользователя на основе access-токена")
    public ResponseEntity<UserLogged> info(){
        return ResponseEntity.ok(authenticationService.info());
    }

    /* 
    @PathMapping("/changePassword")
    public ResponseEntity<LoginResponse> changePassword(ChangePasswordRequest request) {
        return authenticationService.changePassword(request);
    }
    */
}
