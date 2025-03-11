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
      var tradeOderToExec = TradeOrder.build(input.user_id(), input.assert_code(), input.type(), input.quantity(),
            input.price());

      if (tradeOderToExec.getType().equals("selling")) {
      }

      if (input.type().equals("buying")) {

         var sellingOrders = this.tradeOrderRepo.getOrdersByAcAndType(input.assert_code(), "selling");
         var totalToBuy = tradeOderToExec.getTotalAmount();
         var profitToBuy = 0.0;
         var remainToBuy = totalToBuy;
         var remainAccetsToBuy = tradeOderToExec.getQuantity();

         for (TradeOrder sellOrder : sellingOrders) {

            /*
             * The amount remains to buy, (if it has no selling orders enoght for this
             * buyer!
             * If there is ao order to buy 1000 but only has 500 in selling orders, the
             * amount remains to buy is 500 (1000 - 500)
             */

            var sellNewQuantity = sellOrder.getQuantity() - tradeOderToExec.getQuantity();

            if (tradeOderToExec.getPrice().compareTo(sellOrder.getPrice()) >= 0) {
               /* Quantity */

               var remainAccetsToSell = sellNewQuantity <= 0 ? 0 : sellNewQuantity;
               
               if (remainAccetsToBuy - sellOrder.getQuantity() <= 0) {
                  remainAccetsToBuy = 0;
               } else {
                  remainAccetsToBuy -= sellOrder.getQuantity();
               }

               /* Price */
               var sellTotal = sellOrder.getTotalAmount();
               remainToBuy -= sellTotal;
               profitToBuy = sellTotal - tradeOderToExec.getTotalAmount() + tradeOderToExec.getTotalAmount();
               if (remainAccetsToSell > 0) {
                  sellTotal = (remainToBuy + sellTotal);
                  sellOrder.updateQuantityAndStatus(remainAccetsToSell, "open", sellTotal);
                  this.tradeOrderRepo.update(sellOrder);
                  continue;
               }
               sellOrder.updateQuantityAndStatus(remainAccetsToSell, "close", sellTotal);
               this.tradeOrderRepo.update(sellOrder);
            }
         }

         if (remainToBuy <= 0) {
            tradeOderToExec.updateQuantityAndStatus(remainAccetsToBuy, "close", profitToBuy);
         }

         if (remainToBuy > 0) {
            tradeOderToExec.updateQuantityAndStatus(remainAccetsToBuy, "open", profitToBuy);
         }
      }
      this.tradeOrderRepo.persiste(tradeOderToExec);
   }

}
