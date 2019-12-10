package com.rpzjava.sqbe.entities.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "sqbe_profile")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;                  // 用户详细信息记录的主键

    String trueName;            // 用户真实姓名
    String nickName;            // 用户在社区的昵称

    @Column(length = 2)
    Integer sex;                // 用户性别 0女 1男

    @Column(length = 50)
    String bio;                 // 用户的一句话简介

    @Column(length = 50)
    String avatarUrl;           // 用户头像地址

}
