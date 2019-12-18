package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.Post;
import com.rpzjava.sqbe.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPostCommentDAO extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPost(Post post);

}
