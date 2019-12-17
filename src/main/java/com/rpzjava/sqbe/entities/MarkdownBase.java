package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkdownBase extends BaseEntity {

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    String source;              // 帖子源内容 mdValue

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    String content;             // 帖子内容 htmlValue

}
