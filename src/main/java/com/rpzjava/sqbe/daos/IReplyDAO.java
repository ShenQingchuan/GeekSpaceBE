package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.Reply;
import com.rpzjava.sqbe.tools.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IReplyDAO extends JpaRepository<Reply, Long> {

    Optional<Reply> findByIdAndStatus(Long id, EntityStatus status);

}
