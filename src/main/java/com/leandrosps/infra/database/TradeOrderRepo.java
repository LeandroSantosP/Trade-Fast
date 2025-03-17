package com.leandrosps.infra.database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.leandrosps.domain.TradeOrder;
import com.leandrosps.exceptions.NotFoundException;

public class TradeOrderRepo implements Repository<TradeOrder, UUID> {

   List<TradeOrder> storage = new ArrayList<>();

   @Override
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

   @Override
   public void update(TradeOrder orderUpdated) {
      for (int i = 0; i < this.storage.size(); i++) {
         if (this.storage.get(i).getId().equals(orderUpdated.getId())) {
            this.storage.set(i, orderUpdated);
            return;
         }
      }
      throw new RuntimeException("Order not found!");
   }

   @Override
   public TradeOrder getById(UUID id) {
      return this.storage.stream().filter(oT -> oT.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
   }

   @Override
   public List<TradeOrder> list() {
      return this.storage;
   }

   @Override
   public void delete(UUID id) {
      var trade = this.getById(id);
      this.storage.remove(trade);
   }
}
