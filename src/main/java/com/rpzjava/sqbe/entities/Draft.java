package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Table(name = "sqbe_draft")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Draft extends PostBase {

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "sqbe_rel_draft_tags",
        joinColumns = @JoinColumn(name = "draft_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tagSet = new HashSet<>();

}
