package com.leandrosps.infra.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import com.leandrosps.domain.TradeOrder;
import com.leandrosps.domain.User;

public class JooqUnityOfWorkDatabase implements Repositories {

  private Connection connection = null;

  private UserDAO userRepository;
  private TradeOrderRepo tradeOrderRepository;

  public JooqUnityOfWorkDatabase(
      UserDAO userRepository,
      TradeOrderRepo tradeRepository,
      Connection connection) {
    this.connection = connection;
    this.userRepository = userRepository;
    this.tradeOrderRepository = tradeRepository;
  }

  @Override
  public Repository<User, UUID> userRepository() {
    return this.userRepository;
  }

  @Override
  public Repository<TradeOrder, UUID> tradeOrderRepository() {
    if (userRepository != null) {
      return this.tradeOrderRepository;
    }
    return this.tradeOrderRepository = new TradeOrderRepo();
  }

  @Override
  public void begin() {
    try {
      this.connection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to start transaction", e);
    }
  }

  @Override
  public void commit() {
    try {
      if (connection != null) {
        this.connection.commit();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to commit transaction", e);
    }
  }

  @Override
  public void rollback() {
    try {
      System.out.println("Rolling back transaction");
      this.connection.rollback();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to rollback transaction", e);
    }
  }

}