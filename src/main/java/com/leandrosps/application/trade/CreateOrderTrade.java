package com.leandrosps.application.trade;

import com.leandrosps.domain.TradeOrder;
import com.leandrosps.dtos.CreateOrderTradeInput;
import com.leandrosps.infra.database.TradeOrderRepo;

public class CreateOrderTrade {

   private TradeOrderRepo tradeOrderRepo;

   public CreateOrderTrade(TradeOrderRepo tradeOrderRepo) {
      this.tradeOrderRepo = tradeOrderRepo;
   }

   public void execute(CreateOrderTradeInput input) {

      var tradeOder = TradeOrder.build(input.user_id(), input.assert_code(), input.type(), input.quantity(),
            input.price());

      if (input.type().equals("selling")) {

      } else if (input.type().equals("buying")) {

      } else {
         throw new RuntimeException("Invalid Order Operation!");
      }

      this.tradeOrderRepo.persiste(tradeOder);
   }

}
