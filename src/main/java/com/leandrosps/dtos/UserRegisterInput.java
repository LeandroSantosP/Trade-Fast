package com.leandrosps.dtos;

import lombok.NonNull;

public record UserRegisterInput(@NonNull String fristName, @NonNull String lastName, @NonNull String email,
        @NonNull String password) {
}