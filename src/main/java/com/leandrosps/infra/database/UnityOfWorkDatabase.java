package com.leandrosps.infra.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import com.leandrosps.domain.TradeOrder;
import com.leandrosps.domain.User;
import com.leandrosps.infra.configs.ConnectionCustom;

public class UnityOfWorkDatabase implements Repositories {

  Connection connection;
  ConnectionCustom connectionCustom;
  UserDAO userRepository;
  private TradeOrderRepo tradeOrderRepository;

  public UnityOfWorkDatabase(
      UserDAO userRepository,
      TradeOrderRepo tradeRepository,
      Connection connection,
      ConnectionCustom connectionCustom) {
    this.connection = connection;
    this.connectionCustom = connectionCustom;
    this.userRepository = userRepository;
    this.tradeOrderRepository = tradeRepository;
  }

  @Override
  public Repository<User, UUID> userRepository() {
    if (userRepository != null) {
      return this.userRepository;
    }
    return this.userRepository = UserRepoDatabase.getInstance(this.connection, this.connectionCustom);
  }

  @Override
  public Repository<TradeOrder, UUID> tradeOrderRepository() {
    if (userRepository != null) {
      return this.tradeOrderRepository;
    }
    return this.tradeOrderRepository = new TradeOrderRepo();
  }

  @Override
  public void beginTransaction() {
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to begin transaction", e);
    }
  }

  @Override
  public void commit() {
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to commit transaction", e);
    }
  }

  @Override
  public void rollback() {
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to rollback transaction", e);
    }
  }

}