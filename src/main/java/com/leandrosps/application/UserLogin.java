package com.leandrosps.application;

import com.leandrosps.infra.UserDAO;

public class UserLogin {

    private UserDAO userDAO;
    
    public UserLogin(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void excute(String username, String password) {
        
    }

}
