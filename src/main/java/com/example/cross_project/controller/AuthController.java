package com.example.cross_project.controller;


import com.example.cross_project.dto.LoginRequest;
import com.example.cross_project.service.AuthenticationService;
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
    public ResponseEntity<LoginResponse> login(
        @CookieValue(name="access-token", required = false) String access,
        @CookieValue(name="refresh-token", required = false) String refresh,
        @RequestBody LoginRequest loginRequest) {
            return authenticationService.login(loginRequest, access, refresh);
        }
    
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
        @CookieValue(name = "refresh-token", required = false) String refresh) {
            return authenticationService.refresh(refresh);
        }
    
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(
        @CookieValue(name = "access-token", required = false) String access) {
            return authenticationService.logout(access);
        }
    
    @GetMapping("/info")
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
