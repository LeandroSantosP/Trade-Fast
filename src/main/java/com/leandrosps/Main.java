package com.leandrosps;

import com.google.gson.Gson;
import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.http.HttpClient;
import com.leandrosps.http.HttpClientSparkJava;
import com.leandrosps.http.HttpMethods;

public class Main {

    private static UserDAOInMemory userDAO = new UserDAOInMemory();
    private static UserService userService = new UserService(userDAO);
    private static HttpClient http = new HttpClientSparkJava();

    public static void main(String[] args) {
        http.lisen(3001);
        http.on(HttpMethods.POST, "/auth/register", (params, data) -> {
            var input = new Gson().fromJson(data, UserRegisterInput.class);
            UserRegister userRegister = new UserRegister(userDAO);
            return userRegister.excute(input);
        });

        http.on(HttpMethods.GET, "/auth/:user_id", (params, data) -> {
            var user_id = params.get(":user_id");
            return userService.getUser(user_id);
        });

       http.on(HttpMethods.GET,"/auth/list/all", (req, res) -> {
            return userService.list();
        });
    }
}