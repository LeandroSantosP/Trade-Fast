package com.leandrosps.http;

import java.util.Map;

@FunctionalInterface
public interface Callback {
     public Object handle(Map<String, String> params, String data);
}