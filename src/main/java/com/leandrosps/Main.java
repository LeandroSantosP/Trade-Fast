package com.leandrosps;

import com.leandrosps.application.TokenHandler;
import com.leandrosps.application.UserLogin;
import com.leandrosps.application.UserService;
import com.leandrosps.http.HttpClient;
import com.leandrosps.http.SparkJavaAdapter;
import com.leandrosps.http.filters.AuthFilter;
import com.leandrosps.infra.UserController;
import com.leandrosps.infra.UserDAOInMemory;

/* Entry Point */
public class Main {

    private static TokenHandler tokenHandler = new TokenHandler();
    private static UserDAOInMemory userDAO = new UserDAOInMemory();
    private static UserService userService = new UserService(userDAO);
    private static HttpClient http = new SparkJavaAdapter();
    private static UserLogin userLogin = new UserLogin(userDAO, tokenHandler);

    public static void main(String[] args) {
        http.lisen(3001);
        http.registerFilter("/api/priv/*", new AuthFilter(tokenHandler));
        new UserController(http, userService, userLogin, userDAO, tokenHandler);
    }
}