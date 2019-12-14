package com.rpzjava.sqbe.entities;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class PostBase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;                    // 帖子Id作为主键

    @Column(nullable = false)
    String title;               // 帖子标题

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    String source;              // 帖子源内容 mdValue

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    String content;             // 帖子内容 htmlValue

    @Column(length = 50)
    String coverUrl;            // 封面图地址

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    UserEntity sender;          // 发送者

}
