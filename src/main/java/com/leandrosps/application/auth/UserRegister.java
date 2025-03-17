package com.leandrosps.application.auth;

import com.leandrosps.domain.User;
import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.exceptions.UserUnauthorizedException;
import com.leandrosps.infra.database.Repositories;
import com.leandrosps.infra.database.UserDAO;

public class UserRegister {

    private UserDAO userDAO;
    private Repositories uow;

    public UserRegister(Repositories uow) {
        this.uow = uow;
        this.userDAO = (UserDAO) uow.userRepository();
    }

    public String excute(UserRegisterInput input) {
        try {
            this.uow.beginTransaction();
            var userOp = this.userDAO.getUserByEmail(input.email());
            if (userOp.isPresent()) {
                throw new UserUnauthorizedException("Invalid Credentials!");
            }
            var user = User.create(input.fristName(), input.lastName(), input.email(), input.password());
            this.userDAO.persiste(user);
            System.out.println("HERE: " + this.userDAO.getById(user.getId()).getEmail());
            return user.getId().toString();
        } catch (Exception e) {
            this.uow.rollback();
            throw e;
        } finally {
            this.uow.commit();
        }
    }
}
