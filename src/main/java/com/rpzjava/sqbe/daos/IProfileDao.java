package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.entities.UserProfile;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface IProfileDao extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findAllById(int id);

//    /**
//     * 对teaPoint执行加1操作
//     * @param sicnuid 学号
//     */
//    @Transactional
//    @Modifying
//    @Query(value="update UserProfile set teaPoint =teaPoint+1 where id = (select uid from UserEntity where sicnuid =:sicnuid)")
//    int addTeaPoint(@Param("sicnuid") String sicnuid);

//    /**
//     * 对teaPoint执行减1操作
//     * @param sicnuid
//     */
//    @Transactional
//    @Modifying
//    @Query(value="update UserProfile set teaPoint =teaPoint-1 where id =(select uid from UserEntity where sicnuid =:sicnuid)")
//    int reduceTeaPoint(@Param("sicnuid") String sicnuid);

    @Transactional
    @Modifying//分好后不要有空格
    @Query(value="update UserProfile set bio =:bio,avatarUrl =:avatar, nickName =:name, sex =:sex where id =:id")
    int updateData(@Param("id") Long id, @Param("avatar_url") String avatar, @Param("bio") String bio, @Param("nick_name") String name, @Param("sex") Integer sex);

//    @Transactional
//    @Modifying//分好后不要有空格
//    @Query(value="update UserProfile set bio =:bio, nickName =:name, sex =:sex where id =(select uid from UserEntity where sicnuid =:sicnuid)")
//    int updateDataNotAvatar(@Param("sicnuid") String sicnuid, @Param("bio") String bio, @Param("nick_name") String name, @Param("sex") String sex);
}
