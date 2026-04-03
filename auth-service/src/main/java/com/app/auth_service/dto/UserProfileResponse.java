package com.app.auth_service.dto;

public record UserProfileResponse(
        String email,
        String name,
        String picture,
        String provider
) {
}
