package com.leandrosps.http;

public interface HttpClient {
    public void on(HttpMethods method, String path, Callback callback);

    public void lisen(Integer port);

    public void close();
}