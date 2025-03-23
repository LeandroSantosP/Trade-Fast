package com.leandrosps.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeOrder {

   private UUID id;
   private String assetCode;
   private String type;
   private Integer quantity;
   private Double price;
   private Double profit;
   private UUID orwerId;
   private String status;
   private LocalDateTime createdAt;

   public TradeOrder(UUID id, String assetCode, String type, Integer quantity, Double price, Double profit,
         UUID orwerId, String status, LocalDateTime createdAt) {
      this.id = id;
      this.assetCode = assetCode;
      this.type = type;
      this.quantity = quantity;
      this.price = price;
      this.profit = profit;
      this.orwerId = orwerId;
      this.status = status;
      this.createdAt = createdAt;
   }

   public static TradeOrder build(UUID user_id, String asset_code, String type, Integer quantity, Double price) {
      return new TradeOrder(UUID.randomUUID(), asset_code, type, quantity, price, 0.0, user_id, "open",
            LocalDateTime.now());
   }

   public void updateQuantityAndStatus(Integer newQuantity, String status, Double newProfit) {
      this.quantity = newQuantity;
      this.status = status;
      this.profit = newProfit;
   }

   public Double getTotalAmount() {
      return this.quantity * this.price;
   }

   public Double calculateProfit(Double total) {
      return total - this.getTotalAmount() + this.getTotalAmount();
   }

   public Double getTotalByQuantity(Double quantity) {
      return quantity * this.price;
   }
}