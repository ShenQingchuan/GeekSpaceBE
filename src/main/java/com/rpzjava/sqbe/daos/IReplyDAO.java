package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReplyDAO extends JpaRepository<Reply, Long> {
}
