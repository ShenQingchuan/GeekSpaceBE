package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProfileDao extends JpaRepository<UserProfile,Long> {
    List<UserProfile> findAll();
}
