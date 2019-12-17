package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostCommentDAO extends JpaRepository<PostComment, Long> {
}
