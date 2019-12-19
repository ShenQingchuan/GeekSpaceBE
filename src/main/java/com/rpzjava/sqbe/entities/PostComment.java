package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "sqbe_rel_comment_reply", joinColumns = {
        @JoinColumn(name = "comment_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
        @JoinColumn(name = "reply_id", referencedColumnName = "id")
    })
    Set<Reply> replySet = new HashSet<>();
}
