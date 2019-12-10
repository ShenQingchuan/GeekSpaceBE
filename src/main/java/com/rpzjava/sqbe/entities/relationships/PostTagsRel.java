package com.rpzjava.sqbe.entities.relationships;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "sqbe_rel_post_tags")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTagsRel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long postId;
    Long tagId;
}
