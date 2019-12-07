package com.rpzjava.sqbe.daos;


import com.rpzjava.sqbe.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUpdateDao extends JpaRepository<UserProfile, Long> {

}
