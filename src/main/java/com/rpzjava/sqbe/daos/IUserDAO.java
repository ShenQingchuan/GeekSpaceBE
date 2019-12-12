package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserDAO extends PagingAndSortingRepository<UserEntity, Long> {

    List<UserEntity> findAll();
    Page<UserEntity> findAll(Pageable pageable);
    Optional<UserEntity> findById(Long uid);

    /**
     * 根据用户学号查询
     */
    Optional<UserEntity> findOneBySicnuid(String sicnuid);

}
