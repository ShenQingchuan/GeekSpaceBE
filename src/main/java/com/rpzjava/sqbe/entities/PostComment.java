package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Table(name = "sqbe_post_comment")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostComment extends MarkdownBase {

    @OneToOne
    @JoinColumn(name = "of_post", referencedColumnName = "id")
    Post post;          // 评论所属的帖子

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    UserEntity sender;          // 此条回复消息 的发送者

}
