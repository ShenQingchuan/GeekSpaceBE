package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.pojos.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITagDAO extends JpaRepository<Tag, Long> {

    @Override
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);

}
