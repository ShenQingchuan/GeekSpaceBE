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
@Table(name = "sqbe_reply")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends MarkdownBase {

    @OneToOne
    @JoinColumn(name = "of_comment", referencedColumnName = "id")
    PostComment postComment;    // 表明是回复哪条评论

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    UserEntity sender;          // 此条回复消息 的发送者

}
