package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.pojos.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostDao extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable);

}
