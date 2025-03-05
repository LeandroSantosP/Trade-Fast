package com.leandrosps.infra.http.filters;


import spark.Request;
import spark.Response;
import spark.Service;

@FunctionalInterface
public interface CustomFilter {
   void handle(Request request, Response response, Service service);
}