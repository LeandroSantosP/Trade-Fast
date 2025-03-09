package com.leandrosps.application.trade;

import java.util.List;

import com.leandrosps.dtos.GetAssetsOrdersOutput;
import com.leandrosps.infra.database.TradeOrderRepo;

public class GetAssetsOrders {

   private TradeOrderRepo tradeOrderRepo;

   public GetAssetsOrders(TradeOrderRepo tradeOrderRepo) {
      this.tradeOrderRepo = tradeOrderRepo;
   }

   public List<GetAssetsOrdersOutput> execute(String asset_code) {
      return this.tradeOrderRepo.getOrdersByAccetCode(asset_code).stream().map(order -> {
         return new GetAssetsOrdersOutput(order.getId(), order.getAssetCode(), order.getType(), order.getQuantity(),
               order.getPrice(), order.getOrwerId(), order.getCreatedAt());
      }).toList(); /* Im proud of that probably not, but it works 👍 */
   }
}
