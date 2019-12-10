package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.pojos.Draft;
import com.rpzjava.sqbe.entities.pojos.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDraftDAO extends JpaRepository<Draft, Long> {

    Optional<Draft> findBySender(UserEntity userEntity);

}
