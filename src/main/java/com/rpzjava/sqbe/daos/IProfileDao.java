package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfileDao extends JpaRepository<UserProfile, Long> {

    @Override
    Optional<UserProfile> findById(Long id);
}
