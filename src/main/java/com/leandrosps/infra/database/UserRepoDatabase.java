package com.leandrosps.infra.database;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Table;

import static org.jooq.impl.DSL.*;

import com.leandrosps.application.Roles;
import com.leandrosps.application.User;

public class UserRepoDatabase implements UserDAO {

   private DSLContext dslContext;

   public UserRepoDatabase(Connection connection) {
      dslContext = using(connection, SQLDialect.MYSQL);
   }

   private Table<Record> usersTable = table("users");

   /* Mutaions */
   @Override
   public void persiste(User user) {

      String roleId = null;

      var rolesResults = this.dslContext.select(table("roles")).fetch();

      for (Record1<Record> rolesResult : rolesResults) {
         if (user.getRoles().contains(rolesResult.get("value"))) {
            roleId = rolesResult.get("id", String.class);
         }
      }

      this.dslContext.insertInto(usersTable,
            field("id"),
            field("frist_name"),
            field("last_name"),
            field("email"),
            field("password"),
            field("salt"),
            field("fk_role_id"),
            field("created_at"))
            .values(
                  user.getId(),
                  user.getFirstName(),
                  user.getLastName(),
                  user.getEmail(),
                  user.getPassword(),
                  user.getSalt(),
                  roleId,
                  user.getCreatedAt())
            .execute();
   }

   @Override
   public void delete(String id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
   }

   @Override
   public void clear() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'clear'");
   }

   /* Query */

   @Override
   public User getUser(String id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'getUser'");
   }

   @Override
   public Optional<User> getUserByEmail(String id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'getUserByEmail'");
   }

   @Override
   public List<User> list() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'list'");
   }

}
