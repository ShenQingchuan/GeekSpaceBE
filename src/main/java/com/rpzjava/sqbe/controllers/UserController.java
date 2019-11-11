package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.dao.UserRepository;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.utils.JwtUtils;
import com.rpzjava.sqbe.utils.RedisUtils;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final RedisUtils redisUtils;

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository, RedisUtils redisUtils) {
        this.userRepository = userRepository;
        this.redisUtils = redisUtils;
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

        //教师工号
        userEntity.setSicnuid(sicnuid);
        //教师姓名
        userEntity.setName(name);
        //密码 注：使用 Spring Boot 自带加密 对密码进行加密
        userEntity.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        //新增用户 这里调用 Spring Data JPA 自带方法进行新增
        UserEntity save = userRepository.save(userEntity);
        //如果不等于 null 返回我们刚刚定义好的工具类
        if (save != null) {
            log.info("成功添加一名教师用户: " + name + "<" + sicnuid + ">.");
            return ResultUtils.success("操作成功!");
        }
        return ResultUtils.error("操作失败!");
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Object login(@RequestBody String request) {
        JSONObject jsonObject = JSON.parseObject(request);//转 JSON 对象

        String sicnuid = jsonObject.get("sicnuid").toString();
        String password = jsonObject.get("password").toString();

        if (userRepository.countBySicnuid(sicnuid) > 0) {//判断用户是否存在
            UserEntity userEntity = userRepository.getPasswordBySicnuid(sicnuid);//数据库中的密码
            if (DigestUtils.md5DigestAsHex(password.getBytes()).equals(userEntity.getPassword())) {//校验密码是否一致
                String token = JwtUtils.genJsonWebToken(userEntity);//得到 Token
                String name = userEntity.getName();
                redisUtils.set(token, userEntity.getSicnuid(), 60);//登录成功后 把token放到Redis Key 存 token ，value 存用户sicnuid
                //登陆成功后 把token和真实姓名返回
                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("token", token);
                log.info("用户: " + "<" + name + "> 登录成功.");
                return ResultUtils.success(map, "登录成功");//登录成功
            }
            return ResultUtils.error("密码错误，请重新输入");
        } else {
            return ResultUtils.error("该教师工号不存在");
        }

    }

    /**
     * 获取所有用户
     */
    @GetMapping("/findAll")
    public Object findAll() {
        List<UserEntity> all = userRepository.findAll();
        for (UserEntity temp : all) {
            temp.setPassword(null);//对密码进行过滤
        }
        return all;
    }

}
