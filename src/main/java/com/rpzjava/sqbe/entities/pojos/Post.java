package com.rpzjava.sqbe.entities.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sqbe_post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;                    // 帖子Id作为主键

    @OneToOne
    @JoinColumn(name = "postSender", referencedColumnName = "uid")
    UserEntity sender;          // 帖子的发送者

    @Column(nullable = false)
    String title;               // 帖子标题

    @Column(nullable = false)
    String source;              // 帖子源内容 mdValue

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    String content;             // 帖子内容 htmlValue

    @Column(length = 50)
    String coverUrl;            // 封面图地址

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;

}