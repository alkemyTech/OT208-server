package com.alkemy.ong.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IBasicRepository<T, ID> extends JpaRepository<T, ID> {

	List<T> findAllAndSoftDeleteFalse();

	Page<T> findAllAndSoftDeleteFalse(Pageable pageable);

	Optional<T> findByIdAndSoftDeleteFalse(ID id);

	boolean existByIdAndSoftDeleteFalse(ID id);

}
