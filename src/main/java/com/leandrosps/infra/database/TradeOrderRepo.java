package com.leandrosps.infra.database;

import java.util.ArrayList;
import java.util.List;

import com.leandrosps.domain.TradeOrder;

public class TradeOrderRepo {

   List<TradeOrder> storage = new ArrayList<>();

   public void persiste(TradeOrder order) {
      this.storage.add(order);
   }

   public List<TradeOrder> getOrdersByAccetCode(String assetCode) {
      return this.storage.stream().filter(oT -> oT.getAssetCode().equals(assetCode)).toList();
   }
}
