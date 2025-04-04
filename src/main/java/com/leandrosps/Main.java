package com.leandrosps;

import com.leandrosps.application.auth.TokenHandler;
import com.leandrosps.application.auth.UserLogin;
import com.leandrosps.application.auth.UserRegister;
import com.leandrosps.application.auth.UserService;
import com.leandrosps.infra.configs.DbConfig;
import com.leandrosps.infra.controllers.AuthController;
import com.leandrosps.infra.controllers.UserController;
import com.leandrosps.infra.database.JooqUnityOfWorkDatabase;
import com.leandrosps.infra.database.Repositories;
import com.leandrosps.infra.database.TradeOrderRepo;
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

    public static UserDAO dbUser() {
        UserDAOInMemory.getInstance(); /* InMemory */
        var mysql = UserRepoDatabase.getInstance(dbConfig.getDSLContext()); /* MySql */
        return mysql;
    }

    public static TradeOrderRepo dbTrade() {
        return new TradeOrderRepo();
    }

    public static UserDAO dbInMemory() {
        /* For Testing */
        return UserDAOInMemory.getInstance();
    }

    private static Repositories jooqUow = new JooqUnityOfWorkDatabase(dbUser(), dbTrade(), DbConfig.getConnection());

    /* Use Cases */
    private static TokenHandler tokenHandler = new TokenHandler();
    private static UserService userService = new UserService(jooqUow);
    private static UserRegister userRegister = new UserRegister(jooqUow);
    private static UserLogin userLogin = new UserLogin(jooqUow, tokenHandler);

    /* HTTP Server */
    private static HttpClient http = new SparkJavaAdapter();

    public static void main(String[] args) {

        DbConfig.flyway(); /* Load FlayWay */
        http.lisen(3001);

        http.registerFilter("/priv/*", new AuthFilter(tokenHandler)); /* Only authenticated users can access */
        http.registerFilter("/priv/admin/*", new AdminFilter(tokenHandler)); /* Only admins can access */
        /*  */
        new AuthController(http, userLogin, userRegister);
        new UserController(http, userService);
    }
}