package com.leandrosps.infra.http.filters;

import com.leandrosps.application.TokenHandler;

import spark.Request;
import spark.Response;
import spark.Service;

public class AuthFilter implements CustomFilter {
   private TokenHandler tokenHandler;

   public AuthFilter(TokenHandler tokenHandler) {
      this.tokenHandler = tokenHandler;
   }

   @Override
   public void handle(Request request, Response response, Service service) {

      var authHeader = request.headers("Authorization");

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
         service.halt(401, "Not Authorized");
      }
      var token = authHeader.substring(7).trim();;
      var result = tokenHandler.tokenValidation(token);
      
      if (!result.isValid()) {
         service.halt(401, "Not Authorized");
      }

      request.attribute("user_id", result.user_id());
   }

}