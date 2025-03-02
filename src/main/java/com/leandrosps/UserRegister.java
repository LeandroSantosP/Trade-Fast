package com.leandrosps;

import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.exceptions.UserUnauthorizedException;

public class UserRegister {

    private UserDAO userDAO;

    public UserRegister(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String excute(UserRegisterInput input) {
        var userOp = this.userDAO.getUserByEmail(input.email());

        if (userOp.isPresent()) {
            throw new UserUnauthorizedException("Invalid Credentials!");
        }
        var user = User.create(input.fristName(), input.lastName(), input.email(), input.password());
        this.userDAO.persiste(user);
        return user.getId().toString();
    }

}
