package com.leandrosps.infra.database;

import java.util.UUID;

import com.leandrosps.domain.TradeOrder;
import com.leandrosps.domain.User;

public interface Repositories {
   Repository<User, UUID> userRepository();
   Repository<TradeOrder, UUID> tradeOrderRepository();

   void begin();
   void commit();
   void rollback();
 }
 