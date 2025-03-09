package com.leandrosps.infra.configs;

import static org.jooq.impl.DSL.using;

import java.sql.SQLException;

import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.SQLDialect;

public class JooqAdapter implements ConnectionCustom {
   private DSLContext dslContext;
   private DataSource dataSource;

   public JooqAdapter(DataSource connection) {
      dslContext = using(connection, SQLDialect.MYSQL);
      this.dataSource = connection;
   }

   @Override
   public Result<Record> query(String sql, Object data) {
      return this.dslContext.fetch(sql.trim().toLowerCase(), data);
   }

   @Override
   public void close() {
      try {
         this.dataSource.getConnection().close();
      } catch (SQLException e) {
         System.out.println("ERROR ON CLOSED THE DB CONNECTION! " + e.getMessage());
         e.printStackTrace();
      }
   }

   @Override
   public Integer mutation(String sql, Object data) {
      return this.dslContext.query(sql, data).execute();
   }

}
