package com.rpzjava.sqbe.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class PostBase extends MarkdownBase {

    @Column(nullable = false)
    String title;               // 帖子标题

    @Column(length = 50)
    String coverUrl;            // 封面图地址

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    UserEntity sender;          // 发送者

}
