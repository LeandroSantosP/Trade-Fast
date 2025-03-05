package com.leandrosps.infra.http.filters;

import com.leandrosps.application.TokenHandler;

import spark.Request;
import spark.Response;
import spark.Service;

public class AdminFilter implements CustomFilter {
   private TokenHandler tokenHandler;

   public AdminFilter(TokenHandler tokenHandler) {
      this.tokenHandler = tokenHandler;
   }

   @Override
   public void handle(Request request, Response response, Service service) {

      var authHeader = request.headers("Authorization");
      var token = authHeader.substring(7).trim();
      var result = tokenHandler.tokenValidation(token);

      System.out.println("USER_ROLES" + result.roles());

      if (!result.roles().contains("admin")) {
         throw new RuntimeException("Not authorized!");
      }
   }

}