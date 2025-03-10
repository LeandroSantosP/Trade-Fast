package com.leandrosps.infra.http;

import com.google.gson.Gson;
import com.leandrosps.dtos.StandardResponse;
import com.leandrosps.dtos.StatusReponse;
import com.leandrosps.exceptions.NotFoundException;
import com.leandrosps.exceptions.UserUnauthorizedException;
import com.leandrosps.infra.http.filters.CustomFilter;

import spark.Request;
import spark.Response;
import spark.Service;

public class SparkJavaAdapter implements HttpClient {

    private Service http;

    public SparkJavaAdapter() {
        this.http = Service.ignite();
        this.handleExceptions();
    }

    @Override
    public void lisen(Integer port) {
        this.http.port(port);
        System.out.println("🚀 Server running on Port -> " + port);
    }

    @Override
    public void close() {
    }

    @Override
    public void on(HttpMethods method, String path, Callback callback) {

        switch (method) {
            case GET:
                this.http.get(path, (req, res) -> handleRequest(callback, req, res));
                break;
            case POST:

                this.http.post(path, (req, res) -> handleRequest(callback, req, res));
                break;
            case PUT:
                this.http.put(path, (req, res) -> handleRequest(callback, req, res));
                break;
            case DELETE:
                this.http.delete(path, (req, res) -> handleRequest(callback, req, res));
                break;
            default:
                break;
        }
    }

    private void handleExceptions() {

        http.exception(UserUnauthorizedException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            response.body(exception.getMessage());
        });

        http.exception(NotFoundException.class, (exception, request, response) -> {
            response.status(exception.getStatus());
            response.body(exception.getMessage());
        });

        http.exception(RuntimeException.class, (exception, request, response) -> {
            exception.printStackTrace();
            response.status(400);
            response.body(exception.getMessage());
        });

        http.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
            response.status(500);
            response.body("Internal Server Error: " + exception.getMessage());
        });
    }

    private String handleRequest(Callback callback, Request req, Response res) {
        var output = callback.handle(req.params(), req.body(), req.attribute("user_id"));
        res.type("application/json");
        res.status(output.status());

        var response = new Gson()
                .toJson(new StandardResponse(StatusReponse.SUCCESS, new Gson().toJsonTree(output.data())));
        return response;
    }

    @Override
    public void registerFilter(String path, CustomFilter customFilter) {
        this.http.before(path, (request, response) -> {
            customFilter.handle(request, response, this.http);
        });
    }
}

