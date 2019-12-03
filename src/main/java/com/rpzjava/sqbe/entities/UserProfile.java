package com.rpzjava.sqbe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sqbe_user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public
class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;                  // 用户详细信息记录的主键

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userProfile")
    UserEntity userEntity;

    String trueName;            // 用户真实姓名
    String nickName;            // 用户在社区的昵称

    @Column(length = 50)
    String bio;                 // 用户的一句话简介

    String email;               // 用户的邮件地址
    int teapo;                  // 用户的茶点积分

}
