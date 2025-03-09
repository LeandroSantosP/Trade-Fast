package com.leandrosps.application.trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import java.util.UUID;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.leandrosps.dtos.CreateOrderTradeInput;
import com.leandrosps.infra.database.TradeOrderRepo;

public class CreateOrderTradeTest {

   @Test
   @Tag("current")
   void shouleCreateOrdersOfAccestsSells() { 

      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);
      
      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var input = new CreateOrderTradeInput(onwerIdA,"USDC", "selling", 20, 7.50);

      createOrderTrade.execute(input);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(1, output.size());
      var order = output.get(0);
      assertInstanceOf(UUID.class, order.id());
      assertEquals("USDC", order.assetCode());
      assertEquals("selling", order.type());
      assertEquals(20, order.quantity());
      assertEquals(7.50, order.price());

   }
}
