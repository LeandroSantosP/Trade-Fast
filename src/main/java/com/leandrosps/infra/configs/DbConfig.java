package com.leandrosps.infra.configs;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.leandrosps.infra.utils.DbCredentails;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConfig {

   public DataSource getDataSource() {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(DbCredentails.URL);
      config.setUsername(DbCredentails.USER);
      config.setPassword(DbCredentails.PASSWORD);
      config.setDriverClassName(DbCredentails.DRIVER);
      // Optional: set connection pool size or other HikariCP properties
      return new HikariDataSource(config);
   }

   public Connection getConnection() {
      Connection conn = null;
      try {
         conn = this.getDataSource().getConnection();
         System.out.println("Connection with success!");
      } catch (SQLException e) {
         System.out.println("Connection Faild");
         e.printStackTrace();
      }
      return conn;
   }

   public DSLContext createDSLContext() {
      return DSL.using(getDataSource(), SQLDialect.MYSQL);
   }

   public Flyway flyway() {
      Flyway flyway = Flyway.configure()
            .dataSource(this.getDataSource())
            .locations("classpath:db/migration")
            .load();
      flyway.migrate();
   for (MigrationInfo all : flyway.info().all()) {
         System.out.println(all);
   };
      return flyway;
   }

}