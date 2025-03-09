package com.leandrosps.infra.controllers;

import com.google.gson.Gson;
import com.leandrosps.application.auth.UserRegister;
import com.leandrosps.application.auth.UserLogin;
import com.leandrosps.dtos.UserLoginInput;
import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.infra.controllers.UserController.Test;
import com.leandrosps.infra.http.HandlerOutput;
import com.leandrosps.infra.http.HttpClient;
import com.leandrosps.infra.http.HttpMethods;

public class AuthController {
   public AuthController(HttpClient http, UserLogin userLogin, UserRegister userRegister) {

      http.on(HttpMethods.POST, "/auth/register", (params, data, user_id) -> {
         var input = new Gson().fromJson(data, UserRegisterInput.class);
         return new HandlerOutput(userRegister.excute(input), 201);
      });

      http.on(HttpMethods.POST, "/auth/singin", (params, data, user_id) -> {
         var input = new Gson().fromJson(data, UserLoginInput.class);
         var output = userLogin.excute(input.email(), input.password());
         return new HandlerOutput(output, 201);
      });

      http.on(HttpMethods.POST, "/auth/valid-user-auth", (params, data, user_id) -> {
         var input = new Gson().fromJson(data, Test.class);
         var output = userLogin.validatedUserAuth(input.token());
         return new HandlerOutput(output.message(), 201);
      });
   }
}
