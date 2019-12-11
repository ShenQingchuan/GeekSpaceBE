package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.pojos.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPostDao extends JpaRepository<Post, Long> {

    @Override
    Optional<Post> findById(Long pid);

    Page<Post> findAllByStatus(int status, Pageable pageable);

}
