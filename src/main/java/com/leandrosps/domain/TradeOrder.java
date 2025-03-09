package com.leandrosps.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TradeOrder {
   private UUID id;
   private String assetCode;
   private String type;
   private Integer quantity;
   private Double price;
   private UUID orwerId;
   private LocalDateTime createdAt;

   public static TradeOrder build(UUID user_id, String assert_code, String type, Integer quantity, Double price) {
      return new TradeOrder(UUID.randomUUID(), assert_code, type, quantity, price, user_id, LocalDateTime.now());
   }
}