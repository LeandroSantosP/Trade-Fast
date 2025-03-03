package com.leandrosps.infra;

import com.google.gson.Gson;
import com.leandrosps.application.UserLogin;
import com.leandrosps.application.UserRegister;
import com.leandrosps.application.UserService;
import com.leandrosps.dtos.UserLoginInput;
import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.http.HandlerOutput;
import com.leandrosps.http.HttpClient;
import com.leandrosps.http.HttpMethods;

public class AuthController {
  public AuthController(HttpClient http, UserService userService, UserDAO userDAO) {
    http.on(HttpMethods.POST, "/auth/register", (params, data) -> {
      var input = new Gson().fromJson(data, UserRegisterInput.class);
      UserRegister userRegister = new UserRegister(userDAO);
      return new HandlerOutput(userRegister.excute(input), 201);
    });

    http.on(HttpMethods.POST, "/auth/singin", (params, data) -> {
      var input = new Gson().fromJson(data, UserLoginInput.class);
      var userLogin = new UserLogin(userDAO);
      var output = userLogin.excute(input.email(), input.password());
      return new HandlerOutput(output, 201);
    });

    http.on(HttpMethods.GET, "/auth/:user_id", (params, data) -> {
      var user_id = params.get(":user_id");
      return new HandlerOutput(userService.getUser(user_id), 200);
    });

    http.on(HttpMethods.GET, "/auth/list/all", (req, res) -> {
      return new HandlerOutput(userService.list(), 200);
    });
  }
}
