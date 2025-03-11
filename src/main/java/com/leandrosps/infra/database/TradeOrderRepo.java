package com.leandrosps.infra.database;

import java.util.ArrayList;
import java.util.List;

import com.leandrosps.domain.TradeOrder;

public class TradeOrderRepo {

   List<TradeOrder> storage = new ArrayList<>();

   public void persiste(TradeOrder order) {
      this.storage.add(order);
   }

   public List<TradeOrder> getOrdersByAcAndType(String assetCode, String type) {
      return this.storage.stream().filter(oT -> oT.getAssetCode().equals(assetCode) && oT.getType().equals(type))
            .toList();
   }

   public List<TradeOrder> getOrdersByAccetCode(String assetCode) {
      return this.storage.stream().filter(oT -> oT.getAssetCode().equals(assetCode)).toList();
   }

   public void update(TradeOrder orderUpdated) {
      for (int i = 0; i < this.storage.size(); i++) {
         if (this.storage.get(i).getId().equals(orderUpdated.getId())) {
            this.storage.set(i, orderUpdated);
            return;
         }
      }
      throw new RuntimeException("Order not found!");
   }
}
