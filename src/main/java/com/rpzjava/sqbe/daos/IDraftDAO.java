package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.pojos.Draft;
import com.rpzjava.sqbe.entities.pojos.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDraftDAO extends JpaRepository<Draft, Long> {

    List<Draft> findBySender(UserEntity sender);
}
