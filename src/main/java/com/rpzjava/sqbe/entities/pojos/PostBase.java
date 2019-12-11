package com.rpzjava.sqbe.entities.pojos;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@MappedSuperclass
@Data
public class PostBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;                    // 帖子Id作为主键

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
    private Date createTime;    // 创建时间

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;    // 修改时间

    int status = 1;             // 帖子/草稿 状态

    @OneToOne
    @JoinColumn(name = "sender", referencedColumnName = "uid")
    @JSONField(serialize = false)
    @JsonIgnore
    UserEntity sender;          // 发送者

}
