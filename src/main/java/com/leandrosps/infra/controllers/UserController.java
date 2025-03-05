package com.leandrosps.infra.controllers;

import com.leandrosps.application.TokenHandler;
import com.leandrosps.application.UserLogin;
import com.leandrosps.application.UserService;
import com.leandrosps.infra.database.UserDAO;
import com.leandrosps.infra.http.HandlerOutput;
import com.leandrosps.infra.http.HttpClient;
import com.leandrosps.infra.http.HttpMethods;

public class UserController {

	public record Test(String token) {
	}

	public UserController(HttpClient http, UserService userService, UserLogin userLogin, UserDAO userDAO,
			TokenHandler tokenHandler) {

		http.on(HttpMethods.DELETE, "/priv/auth/delete", (params, data, user_id) -> {
			userService.deletUser(user_id);
			return null;
		});

		http.on(HttpMethods.GET, "/priv/auth/:user_id", (params, data, user_id_from) -> {
			var user_id_to = params.get(":user_id");
			return new HandlerOutput(userService.getUser(user_id_from, user_id_to), 200);
		});

		http.on(HttpMethods.GET, "/priv/admin/users/all", (req, res, user_id) -> {
			return new HandlerOutput(userService.list(), 200);
		});
	}

}
