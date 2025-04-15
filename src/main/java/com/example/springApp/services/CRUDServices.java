package com.example.springApp.services;

import java.util.Collection;

public interface CRUDServices<T> {
    T getById(long id);
    Collection<T> getAll();
    void create(T item);
    void delete (long id);
    void update (T item);


}
