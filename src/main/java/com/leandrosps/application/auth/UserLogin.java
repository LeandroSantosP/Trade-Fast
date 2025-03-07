package com.leandrosps.application.auth;

import com.leandrosps.application.auth.TokenHandler.TokenValidationResult;
import com.leandrosps.dtos.UserLoginOuput;
import com.leandrosps.exceptions.UserUnauthorizedException;
import com.leandrosps.infra.database.UserDAO;

public class UserLogin {

    private UserDAO userDAO;
    private TokenHandler tokenHandler;

    public UserLogin(UserDAO userDAO, TokenHandler tokenHandler) {
        this.userDAO = userDAO;
        this.tokenHandler = tokenHandler;
    }

    public UserLoginOuput excute(String email, String password) {
        var userOp = this.userDAO.getUserByEmail(email);

        if (userOp.isEmpty()) {
            throw new UserUnauthorizedException("Passoword or email is invalid!!");
        }

        var user = userOp.get();

        if (!user.isPasswordValid(password)) {
            throw new UserUnauthorizedException("Passoword or email is invalid!!");
        }

        var generateTokenOutput = this.tokenHandler.generateToken(user.getId().toString(), user.getEmail(), user.getRolesAsListString());

        return new UserLoginOuput(generateTokenOutput.token(), generateTokenOutput.expiredAt());
    }

    public TokenValidationResult validatedUserAuth(String token) {
        var result = this.tokenHandler.tokenValidation(token);
        if (!result.isValid()) {
            throw new RuntimeException(result.message());
        }
        return result;
    }

}
