package com.rpzjava.sqbe.entities;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sqbe_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true)
    String sicnuid;

    @Column(nullable = false)
    @JSONField(serialize = false)
    String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    UserProfile userProfile;

}
