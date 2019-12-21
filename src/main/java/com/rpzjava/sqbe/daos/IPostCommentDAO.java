package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.Post;
import com.rpzjava.sqbe.entities.PostComment;
import com.rpzjava.sqbe.tools.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPostCommentDAO extends JpaRepository<PostComment, Long> {

    Page<PostComment> findAllByPostAndStatus(Post post, EntityStatus entityStatus, Pageable pageable);

    Optional<PostComment> findByIdAndStatus(Long id, EntityStatus status);

    long countByPostAndStatus(Post post, EntityStatus status);

}
