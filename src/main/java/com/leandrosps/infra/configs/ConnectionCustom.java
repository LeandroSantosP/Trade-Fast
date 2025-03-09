package com.leandrosps.infra.configs;

public interface ConnectionCustom {
   Integer mutation(String sql, Object data);   
   Object query(String sql, Object data);   
   void close();
}
