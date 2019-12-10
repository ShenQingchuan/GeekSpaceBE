package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.pojos.UserEntity;
import com.rpzjava.sqbe.entities.pojos.UserProfile;
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

    private final IUserDAO iUserDAO;

    public UserController(IUserDAO iUserDAO) {
        this.iUserDAO = iUserDAO;
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
        Optional<UserEntity> userEntitySrc = iUserDAO.findOneBySicnuid(sicnuid); // 在数据库空查询该学号
        if (userEntitySrc.isPresent()) { // 判断用户是否存在
            //返回给前端
            return ResultUtils.error("用户已存在！");
        }

        // 川师学号
        userEntity.setSicnuid(sicnuid);
        // 密码 注：使用 Spring Boot 自带加密 对密码进行加密
        userEntity.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        // 为此用户新增一个信息表
        UserProfile userProfile = new UserProfile();
        userProfile.setTrueName(name);
        userProfile.setNickName(name);  //默认昵称为真实姓名，待用户自行修改
        userProfile.setSex(1);          //默认资料性别为 1（男性）
        userProfile.setBio("这个人好懒，什么也没留下！");
        userEntity.setUserProfile(userProfile);
        // 新增用户 这里调用 Spring Data JPA 自带方法进行新增
        iUserDAO.save(userEntity);
        // 如果不等于 null 返回我们刚刚定义好的工具类
        log.info("成功添加一名用户: " + "<" + sicnuid + ">.");
        return ResultUtils.success("注册成功!");

    }

    /**
     * 按 uid 信息查询用户
     */
    @GetMapping("/{id}")
    public Object findByUserId(@PathVariable String id) {
        Long uid = Long.parseLong(id);
        Optional<UserEntity> foundUser = iUserDAO.findByUid(uid);
        if (foundUser.isPresent()) {
            UserEntity temp = foundUser.get();
            temp.setPassword(null);
            return ResultUtils.success(JSON.toJSON(temp));
        }
        return ResultUtils.error("没有找到 uid: "+ uid + " 的用户");
    }


    /**
     * （分页查询）获取所有用户
     */
    @GetMapping("/all")
    public Object findAll() {
        List<UserEntity> all = iUserDAO.findAll();
        for (UserEntity temp : all) {
            temp.setPassword(null);//对密码进行过滤
        }
        return ResultUtils.success(
            JSONArray.parseArray(JSON.toJSONString(all)),
            "获取用户列表成功"
        );
    }

}
