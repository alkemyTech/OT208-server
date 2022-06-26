package com.alkemy.ong.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BasicService<T, ID> {

    public T save(T t);

    public Optional<T> findById(ID id);

    public List<T> findAll();

    public Page<T> findAll(Pageable pageable);

    public T edit(T t);

    public void delete(T t);

    public void deleteById(ID id);

    public boolean existById(ID id);
}
