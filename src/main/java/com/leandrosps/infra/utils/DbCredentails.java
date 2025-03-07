package com.leandrosps.infra.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class DbCredentails {
   public static final String URL = Dotenv.load().get("DB_URL");;
   public static final String USER = Dotenv.load().get("DB_USER");;
   public static final String PASSWORD = Dotenv.load().get("DB_PASS");;
   public static final String DRIVER = Dotenv.load().get("DB_DRIVER");;
}
