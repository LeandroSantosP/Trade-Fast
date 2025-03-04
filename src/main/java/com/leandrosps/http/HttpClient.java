package com.leandrosps.http;

import com.leandrosps.http.filters.CustomFilter;

public interface HttpClient {
    public void on(HttpMethods method, String path, Callback callback);

    public void lisen(Integer port);

    public void close();

    public void registerFilter(String path, CustomFilter customFilter);
}