package com.leandrosps.exceptions;

import lombok.Getter;

public class NotFoundException extends RuntimeException {
    @Getter
    private Integer status;

    public NotFoundException(String messge) {
        super(messge);
        this.status = 404;
    }

    public NotFoundException() {
        super();
    }
}
