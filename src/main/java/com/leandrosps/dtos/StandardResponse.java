package com.leandrosps.dtos;

import com.google.gson.JsonElement;

import lombok.Getter;

@Getter
public class StandardResponse {
    private StatusReponse status;
    private String message;
    private JsonElement data;
    
    public StandardResponse(StatusReponse status) {
        this.status = status;
    }

    public StandardResponse(StatusReponse status, JsonElement data) {
        this.status = status;
        this.data = data;
        this.message = "OK";
    }

    public StandardResponse(StatusReponse status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public StandardResponse(StatusReponse status, String message, JsonElement data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
