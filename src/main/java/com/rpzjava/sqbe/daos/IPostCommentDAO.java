package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.Post;
import com.rpzjava.sqbe.entities.PostComment;
import com.rpzjava.sqbe.tools.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPostCommentDAO extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPost(Post post);

    Optional<PostComment> findByIdAndStatus(Long id, EntityStatus status);

}
