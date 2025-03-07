package com.leandrosps.infra.database;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;

import com.leandrosps.domain.Roles;
import com.leandrosps.domain.User;
import com.leandrosps.exceptions.NotFoundException;

public class UserRepoDatabase implements UserDAO {

   private DSLContext dslContext;

   public UserRepoDatabase(Connection connection) {
      dslContext = using(connection, SQLDialect.MYSQL);
   }

   private Table<Record> usersTable = table("users");

   /* Mutaions */
   @Override
   public void persiste(User user) {

      var roleResults = this.dslContext
            .select(field("id"))
            .from(table("roles"))
            .where(field("value").eq(user.getMajorRole()))
            .fetch();

      if (roleResults == null || roleResults.isEmpty() ) {
         throw new NotFoundException("Role does not exists!");
      }

      var roleId = roleResults.get(0).get("id", String.class);

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
      /*
       * ✅ Returns a single record if exactly one row matches.
       * ✅ Returns null if no record is found.
       * ❌ Throws an exception if more than one row is found.
       */
      var userData = this.dslContext.select().from(table("users")).where(field("email").eq(id)).fetchOne();

      if (userData == null) {
         return Optional.empty();
      }

      var user_id = userData.get("id", String.class);
      var first_name = userData.get("first_name", String.class);
      var last_name = userData.get("last_name", String.class);
      var email = userData.get("email", String.class);
      var password = userData.get("password", String.class);
      var salt = userData.get("salt", Integer.class);
      var created_at = userData.get("created_at", LocalDateTime.class);

      var roleResults = this.dslContext
            .select(field("value"))
            .from(table("roles"))
            .where(field("id").eq(userData.get("fk_role_id", String.class)))
            .fetchOne();

      var roleData = roleResults.get("value", String.class);

      List<Roles> roles = List.of();

      if (roleData.equals("admin")) {
         roles.add(Roles.ADMIN);
      }

      if (roleData.equals("user")) {
         roles.add(Roles.USER);
      }

      var user = new User(UUID.fromString(user_id), first_name, last_name, email, password, salt, roles, created_at);
      return Optional.of(user);
   }

   @Override
   public List<User> list() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'list'");
   }

}
