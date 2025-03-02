package com.leandrosps;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.leandrosps.dtos.StandardResponse;
import com.leandrosps.dtos.StatusReponse;
import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.exceptions.NotFoundException;
import com.leandrosps.exceptions.UserUnauthorizedException;

public class Main {

    private static UserRegister userRegister = new UserRegister();

    public static void main(String[] args) {
        port(3001);
        post("/auth/register", (req, res) -> {
            res.type("application/json");
            var input = new Gson().fromJson(req.body(), UserRegisterInput.class);
            var output = userRegister.register(input);
            return new Gson().toJson(new StandardResponse(StatusReponse.SUCCESS, output.toString()));
        });

        get("/auth/:user_id", (req, res) -> {
            var user_id = req.params(":user_id");
            var output = userRegister.getUser(user_id);
            return new Gson().toJson(new StandardResponse(StatusReponse.SUCCESS, new Gson().toJsonTree(output)));
        });

        get("/auth/list/all", (req, res) -> {
            return new Gson()
                    .toJson(new StandardResponse(StatusReponse.SUCCESS, new Gson().toJsonTree(userRegister.list())));
        });

        exception(UserUnauthorizedException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            response.body("Internal Server Error: " + exception.getMessage());
        });

        exception(NotFoundException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            response.body("Internal Server Error: " + exception.getMessage());
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body("Internal Server Error: " + exception.getMessage());
        });

    }
}