package com.alkemy.ong.repositories;

import com.alkemy.ong.models.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMemberRepository extends JpaRepository<MemberEntity, String> {

}
