package com.leandrosps.infra.database;

import java.util.List;

public interface Repository<T, ID> {
   T getById(ID id);
   void persiste(T entity);
   List<T> list();
   void update(T entity);
   void delete(ID id);
}