package com.leandrosps;

import com.leandrosps.application.auth.TokenHandler;
import com.leandrosps.application.auth.UserLogin;
import com.leandrosps.application.auth.UserRegister;
import com.leandrosps.application.auth.UserService;
import com.leandrosps.infra.configs.ConnectionCustom;
import com.leandrosps.infra.configs.DbConfig;
import com.leandrosps.infra.configs.JooqAdapter;
import com.leandrosps.infra.controllers.AuthController;
import com.leandrosps.infra.controllers.UserController;
import com.leandrosps.infra.database.UserDAO;
import com.leandrosps.infra.database.UserDAOInMemory;
import com.leandrosps.infra.database.UserRepoDatabase;
import com.leandrosps.infra.http.HttpClient;
import com.leandrosps.infra.http.SparkJavaAdapter;
import com.leandrosps.infra.http.filters.AdminFilter;
import com.leandrosps.infra.http.filters.AuthFilter;

/* Entry Point */
public class Main {

    /* Db Setting */
    private static DbConfig dbConfig = new DbConfig();

    public static UserDAO db() {
        ConnectionCustom jooqAdapter = new JooqAdapter(dbConfig.getDataSource());
        UserDAOInMemory.getInstance(); /* InMemory */
        var mysql = UserRepoDatabase.getInstance(dbConfig.getConnection(), jooqAdapter);  /* MySql */
        return mysql;
    }

    /* Use Cases */
    private static TokenHandler tokenHandler = new TokenHandler();
    private static UserService userService = new UserService(db());
    private static UserRegister userRegister = new UserRegister(db());
    private static UserLogin userLogin = new UserLogin(db(), tokenHandler);
    
    /* HTTP Server */
    private static HttpClient http = new SparkJavaAdapter();
    
    public static void main(String[] args) {
        dbConfig.flyway(); /* Load FlayWay */
        http.lisen(3001);
        
        http.registerFilter("/priv/*", new AuthFilter(tokenHandler)); /* Only authenticated users can access */
        http.registerFilter("/priv/admin/*", new AdminFilter(tokenHandler)); /* Only admins can access */

        new AuthController(http, userLogin, userRegister);
        new UserController(http, userService);
    }
}