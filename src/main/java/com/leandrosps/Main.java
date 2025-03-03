package com.leandrosps;

import com.leandrosps.application.UserService;
import com.leandrosps.http.HttpClient;
import com.leandrosps.http.SparkJavaAdapter;
import com.leandrosps.infra.AuthController;
import com.leandrosps.infra.UserDAOInMemory;

/* Entry Point */
public class Main {

    private static UserDAOInMemory userDAO = new UserDAOInMemory();
    private static UserService userService = new UserService(userDAO);
    private static HttpClient http = new SparkJavaAdapter();

    public static void main(String[] args) {
        http.lisen(3001);
        new AuthController(http, userService, userDAO);
    }
}