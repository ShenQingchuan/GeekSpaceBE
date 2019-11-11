package com.rpzjava.sqbe.dao;

import com.rpzjava.sqbe.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    int countBySicnuid(String sicnuid);

    /**
     * 根据用户名查询密码
     */
    UserEntity getPasswordBySicnuid(String sicnuid);
}
