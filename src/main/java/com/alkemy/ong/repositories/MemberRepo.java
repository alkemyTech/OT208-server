/*
 * 
 */
package com.alkemy.ong.repositories;

import com.alkemy.ong.models.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author acamus
 */
public interface MemberRepo extends JpaRepository<MemberEntity, Long>{
    
}
