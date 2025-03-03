package com.leandrosps.dtos;

import org.jetbrains.annotations.NotNull;

public record UserLoginInput(@NotNull String email,@NotNull String password) {

}