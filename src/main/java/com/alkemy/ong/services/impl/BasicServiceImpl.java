package com.alkemy.ong.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicServiceImpl <T, ID, R extends JpaRepository<T, ID>>{
	
	protected final R repositorio;

	public T save(T t) {
		return repositorio.save(t);
	}

	public Optional<T> findById(ID id) {
		return repositorio.findById(id);
	}

	public List<T> findAll() {
		return repositorio.findAll();
	}

	public Page<T> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	public T edit(T t) {
		return repositorio.save(t);
	}

	public void delete(T t) {
		repositorio.delete(t);
	}

	public void deleteById(ID id) {
		repositorio.deleteById(id);
	}

	public boolean existById(ID id) {
		return repositorio.existsById(id);
	}
}
