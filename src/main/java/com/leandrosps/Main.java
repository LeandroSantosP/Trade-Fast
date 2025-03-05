package com.leandrosps;

import com.leandrosps.application.TokenHandler;
import com.leandrosps.application.UserLogin;
import com.leandrosps.application.UserService;
import com.leandrosps.infra.controllers.AuthController;
import com.leandrosps.infra.controllers.UserController;
import com.leandrosps.infra.database.UserDAOInMemory;
import com.leandrosps.infra.http.HttpClient;
import com.leandrosps.infra.http.SparkJavaAdapter;
import com.leandrosps.infra.http.filters.AdminFilter;
import com.leandrosps.infra.http.filters.AuthFilter;

/* Entry Point */
public class Main {

    private static TokenHandler tokenHandler = new TokenHandler();
    private static UserDAOInMemory userDAO = new UserDAOInMemory();
    private static UserService userService = new UserService(userDAO);
    private static HttpClient http = new SparkJavaAdapter();
    private static UserLogin userLogin = new UserLogin(userDAO, tokenHandler);

    public static void main(String[] args) {
        http.lisen(3001);

        http.registerFilter("/priv/*", new AuthFilter(tokenHandler)); /* Only authenticated users can access */
        http.registerFilter("/priv/admin/*", new AdminFilter(tokenHandler)); /* Only admins can access */

        new AuthController(http, userService, userLogin, userDAO, tokenHandler);
        new UserController(http, userService, userLogin, userDAO, tokenHandler);
    }
}