package com.leandrosps.dtos;

import java.util.Date;

public record GenerateTokenOutput(String token, Date expiredAt) {
}