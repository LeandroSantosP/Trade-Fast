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
         var buyingOrders = this.tradeOrderRepo.getOrdersByAcAndType(input.assert_code(), "buying");
         var profitToSell = 0.0;

         var remainAccetsToSell = tradeOderToExec.getQuantity();

         for (TradeOrder buyOrder : buyingOrders) {
            var buyNewQuantity = buyOrder.getQuantity() - remainAccetsToSell;

            // if the price of the order to sell is less than or equal to the price of the
            // order to buy, then the order to sell can be executed. THE SELL PRICE IS
            // LESS THAN THE BUY PRICE
            if (buyOrder.getPrice().compareTo(tradeOderToExec.getPrice()) >= 0) {
               remainAccetsToSell -= buyOrder.getQuantity();

               if (remainAccetsToSell < 0) {
                  remainAccetsToSell = 0;
               }

               var remainAccetsToBuy = buyNewQuantity <= 0 ? 0 : buyNewQuantity;

               if (remainAccetsToBuy > 0) {
                  var totalWithRemain = buyOrder.getTotalByQuantity((double) remainAccetsToBuy);
                  profitToSell += totalWithRemain;
                  buyOrder.updateQuantityAndStatus(remainAccetsToBuy, "open", totalWithRemain);
                  this.tradeOrderRepo.update(buyOrder);
                  continue;
               }

               var buyTotal = buyOrder.getTotalAmount();
               profitToSell += buyTotal;

               buyOrder.updateQuantityAndStatus(remainAccetsToBuy, "close", buyTotal);
               this.tradeOrderRepo.update(buyOrder);
            }
         }

         if (remainAccetsToSell <= 0) {
            tradeOderToExec.updateQuantityAndStatus(remainAccetsToSell, "close", profitToSell);
         }
      }

      if (input.type().equals("buying")) {
         var sellingOrders = this.tradeOrderRepo.getOrdersByAcAndType(input.assert_code(), "selling").stream()
               .sorted((a, b) -> a.getPrice().compareTo(b.getPrice()))
               .toList();

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

            // if the price of the order to buy is greater than or equal to the price of the
            // order to sell, then the order to buy can be executed. THE SELL PRICE IS
            // GREATER THAN THE BUY PRICE
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
