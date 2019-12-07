package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface IUserDAO extends JpaRepository<UserEntity, Long> {
    /**
     * 根据用户学号查询
     */
    Optional<UserEntity> findBySicnuid(String sicnuid);

}
