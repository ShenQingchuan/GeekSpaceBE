package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.Draft;
import com.rpzjava.sqbe.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDraftDAO extends JpaRepository<Draft, Long> {

    @Query("select d from Draft d where d.sender = :sender and d.status=1")
    List<Draft> findBySender(UserEntity sender);

    @Override
    @Modifying
    @Query("update Draft set status = 0 where id = :id")
    void deleteById(Long id);

}
