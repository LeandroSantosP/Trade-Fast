package com.leandrosps.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetAssetsOrdersOutput(UUID id, String assetCode, String type, Integer quantity, Double price,UUID orwerId, LocalDateTime createdAt) {
}