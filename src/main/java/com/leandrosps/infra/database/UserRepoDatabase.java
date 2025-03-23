package com.leandrosps.infra.database;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;

import com.leandrosps.domain.Roles;
import com.leandrosps.domain.User;
import com.leandrosps.exceptions.NotFoundException;

public class UserRepoDatabase implements UserDAO {

   private DSLContext dslContext;

   private UserRepoDatabase(DSLContext dslContext) {
      this.dslContext = dslContext;
   }

   private Table<Record> USER_T = table("users");

   private static UserRepoDatabase instance = null;

   public static UserRepoDatabase getInstance(DSLContext dslContext) {
      if (instance == null) {
         instance = new UserRepoDatabase(dslContext);
      }
      return instance;
   }

   /* Mutaions */
   @Override
   public void persiste(User user) {
      var roleResults = this.dslContext
            .select(field("id"))
            .from(table("roles"))
            .where(field("roles.value").eq(user.getMajorRole()))
            .fetch();

      if (roleResults == null || roleResults.isEmpty()) {
         throw new NotFoundException("Role does not exists!");
      }

      var roleId = roleResults.get(0).get("id", String.class);

      this.dslContext.insertInto(USER_T,
            field("users.id"),
            field("users.first_name"),
            field("users.last_name"),
            field("users.email"),
            field("users.password"),
            field("users.salt"),
            field("users.fk_role_id"),
            field("users.created_at"))
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
   public void delete(UUID id) {
   }

   @Override
   public void clear() {
      this.dslContext.delete(table("fastDb.users")).execute();
   }

   /* Query */

   @Override
   public Optional<User> getUserByEmail(String user_email) {
      /*
       * ✅ Returns a single record if exactly one row matches.
       * ✅ Returns null if no record is found.
       * ❌ Throws an exception if more than one row is found.
       */
      var userData = this.dslContext.select().from(table("users")).where(field("email").eq(user_email)).fetchOne();
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

      List<Roles> roles = new ArrayList<>();

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
      List<User> users = new ArrayList<>();
      Result<Record> results = this.dslContext.select().from(USER_T).fetch();

      for (Record record : results) {
         var user_id = record.get("id", String.class);
         var first_name = record.get("first_name", String.class);
         var last_name = record.get("last_name", String.class);
         var email = record.get("email", String.class);
         var password = record.get("password", String.class);
         var salt = record.get("salt", Integer.class);
         var created_at = record.get("created_at", LocalDateTime.class);

         var roleResults = this.dslContext
               .select(field("value"))
               .from(table("roles"))
               .where(field("id").eq(record.get("fk_role_id", String.class)))
               .fetchOne();

         var roleData = roleResults.get("value", String.class);

         List<Roles> roles = new ArrayList<>();

         if (roleData.equals("admin")) {
            roles.add(Roles.ADMIN);
         }

         if (roleData.equals("user")) {
            roles.add(Roles.USER);
         }

         var user = new User(UUID.fromString(user_id), first_name, last_name, email, password, salt, roles, created_at);
         users.add(user);
      }

      return users;
   }

   @Override
   public User getById(UUID id) {
      var userData = this.dslContext.select().from(table("users")).where(field("id").eq(id.toString())).fetchOne();
      if (userData == null) {
         throw new NotFoundException("User not found!");
      }

      var user_id = userData.get("id", String.class);
      var first_name = userData.get("first_name", String.class);
      var last_name = userData.get("last_name", String.class);
      var email = userData.get("email", String.class);
      var password = userData.get("password", String.class);
      var salt = userData.get("salt", Integer.class);
      var created_at = userData.get("created_at", LocalDateTime.class);

      return new User(UUID.fromString(user_id), first_name, last_name, email, password, salt, new ArrayList<>(),
            created_at);
   }

   @Override
   public void update(User entity) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'update'");
   }

}
