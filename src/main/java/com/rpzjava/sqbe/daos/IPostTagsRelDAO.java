package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.relationships.PostTagsRel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostTagsRelDAO extends JpaRepository<PostTagsRel, Long> {

}
