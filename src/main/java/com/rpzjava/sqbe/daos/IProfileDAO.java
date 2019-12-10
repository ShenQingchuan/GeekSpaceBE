package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.pojos.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProfileDAO extends JpaRepository<UserProfile,Long> {
    List<UserProfile> findAll();
}
