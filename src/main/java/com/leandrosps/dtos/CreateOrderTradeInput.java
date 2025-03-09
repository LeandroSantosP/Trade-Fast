package com.leandrosps.dtos;

import java.util.UUID;

public record CreateOrderTradeInput(UUID user_id, String assert_code, String type, Integer quantity, Double price) {

}