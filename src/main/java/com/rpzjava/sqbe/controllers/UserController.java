package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.entities.UserProfile;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final com.rpzjava.sqbe.daos.IUserDAO IUserDAO;

    public UserController(com.rpzjava.sqbe.daos.IUserDAO IUserDAO) {
        this.IUserDAO = IUserDAO;
    }

    /**
     * 新增用户
     */
    @PostMapping("/")
    public Object saveUser(@RequestBody JSONObject jsonObject) {
        UserEntity userEntity = new UserEntity();

        String sicnuid = jsonObject.get("sicnuid").toString();
        String name = jsonObject.get("name").toString();
        String password = jsonObject.get("password").toString();
        //新增判断:用户是否已经存在
        Optional<UserEntity> userEntitySrc = IUserDAO.findBySicnuid(sicnuid); // 在数据库空查询该学号
        if (userEntitySrc.isPresent()) { // 判断用户是否存在
            //返回给前端
            //......
            return ResultUtils.error("用户已存在！");
        }

        // 川师学号
        userEntity.setSicnuid(sicnuid);
        // 密码 注：使用 Spring Boot 自带加密 对密码进行加密
        userEntity.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        // 为此用户新增一个信息表
        UserProfile userProfile = new UserProfile();
        userProfile.setTrueName(name);
        userEntity.setUserProfile(userProfile);
        // 新增用户 这里调用 Spring Data JPA 自带方法进行新增
        UserEntity save = IUserDAO.save(userEntity);
        // 如果不等于 null 返回我们刚刚定义好的工具类
        if (save != null) {
            log.info("成功添加一名用户: " + "<" + sicnuid + ">.");
            return ResultUtils.success("操作成功!");
        }
        return ResultUtils.error("操作失败!");
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/findAll")
    public Object findAll() {
        List<UserEntity> all = IUserDAO.findAll();
        for (UserEntity temp : all) {
            temp.setPassword(null);//对密码进行过滤
        }
        return all;
    }

}
