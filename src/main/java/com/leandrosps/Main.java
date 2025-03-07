package com.leandrosps;

import com.leandrosps.application.TokenHandler;
import com.leandrosps.application.UserLogin;
import com.leandrosps.application.UserService;
import com.leandrosps.infra.configs.DbConfig;
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
    private static UserDAO databaseMemory = UserDAOInMemory.getInstance(); /* InMomory */
    UserDAO database = new UserRepoDatabase(dbConfig.getConnection()); /* MySql */

    private static TokenHandler tokenHandler = new TokenHandler();
    private static UserService userService = new UserService(databaseMemory);
    private static HttpClient http = new SparkJavaAdapter();
    private static UserLogin userLogin = new UserLogin(databaseMemory, tokenHandler);

    public static void main(String[] args) {
        dbConfig.flyway(); /* Load FlayWay */
        dbConfig.createDSLContext();

        http.lisen(3001);

        http.registerFilter("/priv/*", new AuthFilter(tokenHandler)); /* Only authenticated users can access */
        http.registerFilter("/priv/admin/*", new AdminFilter(tokenHandler)); /* Only admins can access */
        new AuthController(http, userLogin, databaseMemory, tokenHandler);
        new UserController(http, userService, databaseMemory, tokenHandler);
    }
}