package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Table(name = "sqbe_tags")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseEntity {

    @Column(nullable = false, unique = true)
    String name;    // 帖子、文章标签的内容，必须唯一

}
