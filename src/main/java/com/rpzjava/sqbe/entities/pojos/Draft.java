package com.rpzjava.sqbe.entities.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name = "sqbe_draft")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Draft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "postSender", referencedColumnName = "uid")
    UserEntity sender;          // 草稿的发送者

    @Column(nullable = false)
    String title;               // 草稿标题

    @Column(nullable = false)
    String source;              // 草稿源内容 mdValue

    @Column(columnDefinition = "MEDIUMTEXT",nullable = false)
    String content;             // 草稿内容 htmlValue

    @Column(length = 50)
    String coverUrl;            // 草稿封面图地址

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;

}
