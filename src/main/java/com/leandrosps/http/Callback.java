package com.leandrosps.http;

import java.util.Map;


@FunctionalInterface
public interface Callback {
     public HandlerOutput handle(Map<String, String> params, String data, String user_id);
}