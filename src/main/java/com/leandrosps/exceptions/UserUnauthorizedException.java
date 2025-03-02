package com.leandrosps.exceptions;

import lombok.Getter;

public class UserUnauthorizedException extends IllegalArgumentException { 
       @Getter
    private Integer status;

    public UserUnauthorizedException(String messge) {
        super(messge);
        this.status = 401;
    }

    public UserUnauthorizedException() {
        super();
    }
}
