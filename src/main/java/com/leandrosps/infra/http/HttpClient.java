package com.leandrosps.infra.http;

import com.leandrosps.infra.http.filters.CustomFilter;

public interface HttpClient {
    public void on(HttpMethods method, String path, Callback callback);

    public void lisen(Integer port);

    public void close();

    public void registerFilter(String path, CustomFilter customFilter);
}