/*
 * Ticket OT208-22
 */
package com.alkemy.ong.repositories;

import com.alkemy.ong.models.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Adrian E. Camus <https://acamus79.github.io/>
 */
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{
    
    /**
    * Method to search only the records with the softDelete field set to false, 
    * @return List(Entity)
    */
    @Query("SELECT m FROM MemberEntity m WHERE m.softDelete = false")
    public List<MemberEntity> searchAllNonDeleted();
    
}
