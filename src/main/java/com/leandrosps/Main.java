package com.leandrosps;

import com.google.gson.Gson;
import com.leandrosps.dtos.StandardResponse;
import com.leandrosps.dtos.StatusReponse;
import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.exceptions.NotFoundException;
import com.leandrosps.exceptions.UserUnauthorizedException;

import spark.Service;

public class Main {

    private static UserDAO userDAO = new UserDAOInMemory();
    private static UserService userService = new UserService(userDAO);

    public static void main(String[] args) {
        var http = Service.ignite();
        http.port(3001);
        http.post("/auth/register", (req, res) -> {
            res.type("application/json");
            var input = new Gson().fromJson(req.body(), UserRegisterInput.class);
            UserRegister userRegister = new UserRegister(userDAO);
            var output = userRegister.excute(input);
            return new Gson().toJson(new StandardResponse(StatusReponse.SUCCESS, output.toString()));
        });

        http.get("/auth/:user_id", (req, res) -> {
            var user_id = req.params(":user_id");
            var output = userService.getUser(user_id);
            return new Gson().toJson(new StandardResponse(StatusReponse.SUCCESS, new Gson().toJsonTree(output)));
        });

        http.get("/auth/list/all", (req, res) -> {
            return new Gson()
                    .toJson(new StandardResponse(StatusReponse.SUCCESS, new Gson().toJsonTree(userService.list())));
        });

        http.exception(UserUnauthorizedException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            response.body( exception.getMessage());
        });

        http.exception(NotFoundException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            response.body( exception.getMessage());
        });

        http.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body("Internal Server Error: " + exception.getMessage());
        });
    }
}