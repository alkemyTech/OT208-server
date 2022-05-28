package com.alkemy.ong.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alkemy.ong.repositories.IBasicRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicServiceImpl<T, ID, R extends IBasicRepository<T, ID>> {

	protected final R repositorio;

	public T save(T t) {
		return repositorio.save(t);
	}

	public Optional<T> findByIdTrue(ID id) {
		return repositorio.findById(id);
	}

	public Optional<T> findByIdDeleteFalse(ID id) {
		return repositorio.findByIdAndSoftDeleteFalse(id);
	}

	public List<T> findAll() {
		return repositorio.findAll();
	}

	public List<T> findAllActive() {
		return repositorio.findAllAndSoftDeleteFalse();
	}

	public Page<T> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	public Page<T> findAllSoftDeleteFalse(Pageable pageable) {
		return repositorio.findAllAndSoftDeleteFalse(pageable);
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

	public boolean existByIdWhereSoftDeleteIsFalse(ID id) {
		return this.repositorio.existByIdAndSoftDeleteFalse(id);
	}
}
