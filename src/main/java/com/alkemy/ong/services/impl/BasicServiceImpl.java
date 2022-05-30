package com.alkemy.ong.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alkemy.ong.services.BasicService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements BasicService<T, ID>{

	protected final R repository;

	public T save(T t) {
		return repository.save(t);
	}

	public Optional<T> findById(ID id) {
		return repository.findById(id);
	}

	public List<T> findAll() {
		return repository.findAll();
	}

	public Page<T> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public T edit(T t) {
		return repository.save(t);
	}

	public void delete(T t) {
		repository.delete(t);
	}

	public void deleteById(ID id) {
		repository.deleteById(id);
	}

	public boolean existById(ID id) {
		return repository.existsById(id);
	}
}
