package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserDAO extends JpaRepository<UserEntity, Long> {

    int countBySicnuid(String sicnuid);

    /**
     * 根据用户学号查询
     */
    Optional<UserEntity> findBySicnuid(String sicnuid);

}
