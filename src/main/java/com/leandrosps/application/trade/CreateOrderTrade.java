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
         var profitToBuy = 0.0;
         var remainAccetsToBuy = tradeOderToExec.getQuantity();

         for (TradeOrder sellOrder : sellingOrders) {

            /*
             * The amount remains to buy, (if it has no selling orders enoght for this
             * buyer!
             * If there is ao order to buy 1000 but only has 500 in selling orders, the
             * amount remains to buy is 500 (1000 - 500)
             */

            var sellNewQuantity = sellOrder.getQuantity() - remainAccetsToBuy;

            if (tradeOderToExec.getPrice().compareTo(sellOrder.getPrice()) >= 0) {

               remainAccetsToBuy -= sellOrder.getQuantity();

               if (remainAccetsToBuy < 0) {
                  remainAccetsToBuy = 0;
               }

               var remainAccetsToSell = sellNewQuantity <= 0 ? 0 : sellNewQuantity;

               if (remainAccetsToSell > 0) {
                  var totalWithRemain = sellOrder.getTotalByQuantity((double) remainAccetsToSell);
                  profitToBuy += totalWithRemain;
                  sellOrder.updateQuantityAndStatus(remainAccetsToSell, "open", totalWithRemain);
                  this.tradeOrderRepo.update(sellOrder);
                  continue;
               }

               var sellTotal = sellOrder.getTotalAmount();
               profitToBuy += sellTotal;

               sellOrder.updateQuantityAndStatus(remainAccetsToSell, "close", sellTotal);
               this.tradeOrderRepo.update(sellOrder);
            }
         }

         if (remainAccetsToBuy <= 0) {
            tradeOderToExec.updateQuantityAndStatus(remainAccetsToBuy, "close", profitToBuy);
         }

         if (remainAccetsToBuy > 0) {
            tradeOderToExec.updateQuantityAndStatus(remainAccetsToBuy, "open", profitToBuy);
         }
      }
      this.tradeOrderRepo.persiste(tradeOderToExec);
   }

}
