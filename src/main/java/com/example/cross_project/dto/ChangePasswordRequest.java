package com.example.cross_project.dto;

public record ChangePasswordRequest(String oldPassword, String newPassword, String newAgain) {
    
}
