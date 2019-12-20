package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Table(name = "sqbe_reply")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends MarkdownBase {

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "id")
    UserEntity sender;          // 此条回复消息 的发送者

    String target;

    @OneToOne
    @JoinColumn(name = "mention")
    UserEntity mention;

}
