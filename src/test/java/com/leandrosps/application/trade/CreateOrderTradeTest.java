package com.leandrosps.application.trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.leandrosps.dtos.CreateOrderTradeInput;
import com.leandrosps.infra.database.TradeOrderRepo;

public class CreateOrderTradeTest {
   @Test
   void shouleCreateASellingOrderAccect() {

      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var input = new CreateOrderTradeInput(onwerIdA, "USDC", "selling", 1000, 7.50);

      createOrderTrade.execute(input);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(1, output.size());
      var order = output.get(0);
      assertInstanceOf(UUID.class, order.id());
      assertEquals("USDC", order.assetCode());
      assertEquals("selling", order.type());
      assertEquals(1000, order.quantity());
      assertEquals(7.50, order.price());
   }

   @Test
   void shouldCreataMultipleSellingOrderAssets() {
      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var inputA = new CreateOrderTradeInput(onwerIdA, "USDC", "selling", 1000, 7.50);
      createOrderTrade.execute(inputA);

      UUID onwerIdB = UUID.fromString("1dbcbef3-fb43-42ea-9e95-097d173fb044");
      var inputB = new CreateOrderTradeInput(onwerIdB, "USDC", "selling", 500, 5.60);
      createOrderTrade.execute(inputB);

      UUID onwerIdC = UUID.fromString("981050a6-7e43-4c3e-9b4a-58f4b28367e0");
      var inputC = new CreateOrderTradeInput(onwerIdC, "USDC", "selling", 100, 5.80);
      createOrderTrade.execute(inputC);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(3, output.size());
      var orderA = output.get(0);
      assertInstanceOf(UUID.class, orderA.id());
      assertEquals("USDC", orderA.assetCode());
      assertEquals("selling", orderA.type());
      assertEquals(1000, orderA.quantity());
      assertEquals(7.50, orderA.price());
      assertEquals("open", orderA.status());

      var orderB = output.get(1);
      assertInstanceOf(UUID.class, orderB.id());
      assertEquals("USDC", orderB.assetCode());
      assertEquals("selling", orderB.type());
      assertEquals(500, orderB.quantity());
      assertEquals(5.60, orderB.price());
      assertEquals("open", orderB.status());

      var orderC = output.get(2);
      assertInstanceOf(UUID.class, orderC.id());
      assertEquals("USDC", orderC.assetCode());
      assertEquals("selling", orderC.type());
      assertEquals(100, orderC.quantity());
      assertEquals(5.80, orderC.price());
      assertEquals("open", orderC.status());
   }

   @Test
   void shouleCreateABuyingAssectOrder() {

      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var input = new CreateOrderTradeInput(onwerIdA, "USDC", "buying", 1000, 5.50);

      createOrderTrade.execute(input);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(1, output.size());
      var order = output.get(0);
      assertInstanceOf(UUID.class, order.id());
      assertEquals("USDC", order.assetCode());
      assertEquals("buying", order.type());
      assertEquals(1000, order.quantity());
      assertEquals(5.50, order.price());
      assertEquals("open", order.status());
   }

   @Test
   @DisplayName("🎯 Should craete assets orders of buy and sell with the same prices")
   void shouldCraeteAssetsOrdersOfBuyAndSellWithTheSamePrices() {

      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var inputA = new CreateOrderTradeInput(onwerIdA, "USDC", "selling", 1000, 5.00);
      createOrderTrade.execute(inputA);

      UUID onwerIdB = UUID.fromString("c5ac7efb-e075-449a-afd0-cfab138fd135");
      var inputB = new CreateOrderTradeInput(onwerIdB, "USDC", "buying", 1000, 5.00);
      createOrderTrade.execute(inputB);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(2, output.size());

      var orderA = output.get(0);
      assertEquals("selling", orderA.type());
      assertEquals(0, orderA.quantity()); /* THE ORDER HAS BEEN COMPLETLY SELLED */
      assertEquals(onwerIdA, orderA.orwerId());
      assertEquals(5.00, orderA.price());
      assertEquals("close", orderA.status());

      var orderB = output.get(1);
      assertEquals("buying", orderB.type());
      assertEquals(0, orderB.quantity()); /* THE ORDER HAS BEEN COMPLETLY BUYED */
      assertEquals(onwerIdB, orderB.orwerId());
      assertEquals(5.00, orderB.price());
      assertEquals(5000, orderB.profit());
      assertEquals("close", orderB.status());
   }

   @Test
   void shouldCraeteAssetsOrdersOfSellQuantityOf1000AndBuyQuantityOf500() {

      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var inputA = new CreateOrderTradeInput(onwerIdA, "USDC", "selling", 200, 5.00);
      createOrderTrade.execute(inputA);

      UUID onwerIdB = UUID.fromString("c5ac7efb-e075-449a-afd0-cfab138fd135");
      var inputB = new CreateOrderTradeInput(onwerIdB, "USDC", "buying", 500, 5.00);
      createOrderTrade.execute(inputB);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(2, output.size());

      var orderA = output.get(0);
      assertEquals("selling", orderA.type());
      assertEquals(0, orderA.quantity()); /* THE ORDER HAS BEEN COMPLETLY SELLED */
      assertEquals(onwerIdA, orderA.orwerId());
      assertEquals(5.00, orderA.price());
      assertEquals(1000, orderA.profit());
      assertEquals("close", orderA.status());

      var orderB = output.get(1);
      assertEquals("buying", orderB.type());
      assertEquals(300, orderB.quantity()); /* THE ORDER HAS BEEN COMPLETLY SELLED */
      assertEquals(onwerIdB, orderB.orwerId());
      assertEquals(5.00, orderB.price());
      assertEquals(1000, orderB.profit());
      assertEquals("open", orderB.status());
   }

   @Test
   void shouldNotBuyIfTheOrderSellPriceIsGreaterThanBuyPriceAndCalculateTheProfit() {

      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var inputA = new CreateOrderTradeInput(onwerIdA, "USDC", "selling", 100, 5.50);
      createOrderTrade.execute(inputA);

      UUID onwerIdB = UUID.fromString("c5ac7efb-e075-449a-afd0-cfab138fd135");
      var inputB = new CreateOrderTradeInput(onwerIdB, "USDC", "buying", 500, 5.00);
      createOrderTrade.execute(inputB);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(2, output.size());

      var orderA = output.get(0);
      assertEquals("selling", orderA.type());
      assertEquals(100, orderA.quantity()); /* THE ORDER HAS BEEN COMPLETLY SELLED */
      assertEquals(onwerIdA, orderA.orwerId());
      assertEquals(5.50, orderA.price());
      assertEquals(0, orderA.profit());
      assertEquals("open", orderA.status());

      var orderB = output.get(1);
      assertEquals("buying", orderB.type());
      assertEquals(500, orderB.quantity()); /* THE ORDER HAS BEEN COMPLETLY SELLED */
      assertEquals(onwerIdB, orderB.orwerId());
      assertEquals(5.00, orderB.price());
      assertEquals(0, orderB.profit());
      assertEquals("open", orderB.status());
   }

   @Test
   void shouldBuyIfTheOrderSellIsLessThenTheOrderBuy() {

      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var inputA = new CreateOrderTradeInput(onwerIdA, "USDC", "selling", 100, 4.50);
      createOrderTrade.execute(inputA);

      UUID onwerIdB = UUID.fromString("c5ac7efb-e075-449a-afd0-cfab138fd135");
      var inputB = new CreateOrderTradeInput(onwerIdB, "USDC", "buying", 500, 5.00);
      createOrderTrade.execute(inputB);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(2, output.size());

      var orderA = output.get(0);
      assertEquals("selling", orderA.type());
      assertEquals(0, orderA.quantity()); /* THE ORDER HAS BEEN COMPLETLY SELLED */
      assertEquals(onwerIdA, orderA.orwerId());
      assertEquals(4.50, orderA.price());
      assertEquals(450, orderA.profit());
      assertEquals("close", orderA.status());

      var orderB = output.get(1);
      assertEquals("buying", orderB.type());
      assertEquals(400, orderB.quantity()); /* THE ORDER HAS BEEN COMPLETLY SELLED */
      assertEquals(onwerIdB, orderB.orwerId());
      assertEquals(5.00, orderB.price());
      assertEquals(450, orderB.profit()); /* Buyed in a good deal for 4.50 */
      assertEquals("open", orderB.status());
   }

   @Test
   @Tag("here")
   void shouldCreataMultipleSellingOrderForOneBuyOrder() {
      var tradeOrderRepo = new TradeOrderRepo();

      var createOrderTrade = new CreateOrderTrade(tradeOrderRepo);

      UUID onwerIdA = UUID.fromString("e903c0af-1762-42e4-9317-92229da13cda");
      var inputA = new CreateOrderTradeInput(onwerIdA, "USDC", "selling", 1000, 4.50);
      createOrderTrade.execute(inputA);

      UUID onwerIdB = UUID.fromString("1dbcbef3-fb43-42ea-9e95-097d173fb044");
      var inputB = new CreateOrderTradeInput(onwerIdB, "USDC", "selling", 500, 5.62);
      createOrderTrade.execute(inputB);

      UUID onwerIdC = UUID.fromString("981050a6-7e43-4c3e-9b4a-58f4b28367e0");
      var inputC = new CreateOrderTradeInput(onwerIdC, "USDC", "selling", 100, 5.80);
      createOrderTrade.execute(inputC);

      UUID onwerIdD = UUID.fromString("981050a6-7e43-4c3e-9b4a-58f4b28367e0");
      var inputD = new CreateOrderTradeInput(onwerIdD, "USDC", "buying", 1550, 5.80);
      createOrderTrade.execute(inputD);

      var getOrdersBook = new GetAssetsOrders(tradeOrderRepo);
      var output = getOrdersBook.execute("USDC");

      assertEquals(4, output.size());
      var orderA = output.get(0);
      assertInstanceOf(UUID.class, orderA.id());
      assertEquals("USDC", orderA.assetCode());
      assertEquals("selling", orderA.type());
      assertEquals(0, orderA.quantity());
      assertEquals(4.50, orderA.price());
      assertEquals(4500, orderA.profit());
      assertEquals("close", orderA.status());

      var orderB = output.get(1);
      assertInstanceOf(UUID.class, orderB.id());
      assertEquals("USDC", orderB.assetCode());
      assertEquals("selling", orderB.type());
      assertEquals(0, orderB.quantity());
      assertEquals(5.62, orderB.price());
      assertEquals(2810, orderB.profit());
      assertEquals("close", orderB.status());

      var orderC = output.get(2);
      assertInstanceOf(UUID.class, orderC.id());
      assertEquals("USDC", orderC.assetCode());
      assertEquals("selling", orderC.type());
      assertEquals(50, orderC.quantity()); /* 50 of leftover to buy */
      assertEquals(5.80, orderC.price());
      assertEquals(290, orderC.profit());
      assertEquals("open", orderC.status());

      var orderD = output.get(3);
      System.out.println("HERE: "+orderD.toString());
      assertInstanceOf(UUID.class, orderD.id());
      assertEquals("USDC", orderD.assetCode());
      assertEquals("buying", orderD.type());
      assertEquals(0, orderD.quantity());
      assertEquals(5.80, orderD.price());
      // discount of 1390 ?
      assertEquals(7600, orderD.profit());
      assertEquals("close", orderD.status());
   }
}
