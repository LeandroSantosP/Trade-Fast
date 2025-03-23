package com.leandrosps.infra.configs;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;

import com.leandrosps.infra.utils.DbCredentails;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static org.jooq.impl.DSL.using;

public class DbConfig {

   private static Connection conn = null;

   public static DataSource getDataSource() {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(DbCredentails.URL);
      config.setUsername(DbCredentails.USER);
      config.setPassword(DbCredentails.PASSWORD);
      config.setDriverClassName(DbCredentails.DRIVER);
      // Optional: set connection pool size or other HikariCP properties
      return new HikariDataSource(config);
   }

   public DSLContext getDSLContext() {
      return using(getConnection(), SQLDialect.MYSQL);
   }
   public static Connection getConnection() {
      if (conn != null) {
         return conn;
      }
      try {
         conn = getDataSource().getConnection();
         System.out.println("Connection with success!");
      } catch (SQLException e) {
         System.out.println("Connection Faild");
         e.printStackTrace();
      }
      return conn;
   }

   public static Flyway flyway() {
      Flyway flyway = Flyway.configure()
            .dataSource(getDataSource())
            .locations("classpath:db/migration")
            .load();
      flyway.migrate();
      for (MigrationInfo all : flyway.info().all()) {
         System.out.println(all);
      }
      return flyway;
   }

}