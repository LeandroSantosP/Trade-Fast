package com.leandrosps.dtos;

import java.util.Date;

public record UserLoginOuput(String token, Date expiredAt) {
}