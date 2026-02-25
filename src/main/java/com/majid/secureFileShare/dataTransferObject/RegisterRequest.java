package com.majid.secureFileShare.dataTransferObject;

public record RegisterRequest(
        String email,
        String password) {
}
