package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sqbe_user_post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;                  // 帖子Id作为主键

    @Column(nullable = false, unique = true)
    String sicnuid;

    @Column(nullable = false)
    String title;            // 帖子标题

//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Column(columnDefinition="longblob")
//    byte[] cover;              //封面图
    @Column(length = 50)
    String coverUrl;           // 封面图地址

    @Column(columnDefinition = "text",nullable = false)
    String description;      // 帖子内容

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date releaseDate;

}