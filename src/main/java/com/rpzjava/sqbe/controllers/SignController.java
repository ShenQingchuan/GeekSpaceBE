package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.utils.JwtUtils;
import com.rpzjava.sqbe.utils.RedisUtils;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
public class SignController {

    private final IUserDAO IUserDAO;

    private final RedisUtils redisUtils;

    public SignController(IUserDAO IUserDAO, RedisUtils redisUtils) {
        this.IUserDAO = IUserDAO;
        this.redisUtils = redisUtils;
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Object login(@RequestBody String reqBody, HttpServletResponse response) {

        JSONObject jsonObject = JSON.parseObject(reqBody);// 转 JSON 对象

        String sicnuid = jsonObject.get("sicnuid").toString();
        String password = jsonObject.get("password").toString();

        Optional<UserEntity> userEntitySrc = IUserDAO.findOneBySicnuid(sicnuid); // 数据库中的密码
        if (userEntitySrc.isPresent()) { // 判断用户是否存在
            if (DigestUtils.md5DigestAsHex(password.getBytes()).equals(userEntitySrc.get().getPassword())) { // 校验密码是否一致

                String token = JwtUtils.genJsonWebToken(userEntitySrc.get()); // 得到 Token
                // 登录成功后 把token放到Redis Key 存 token ，value 存用户sicnuid
                redisUtils.set(token, userEntitySrc.get().getId().toString(), JwtUtils.TOKEN_EXPIRE_TIME);

                //登陆成功后 把token和真实姓名返回
                Map<String, Object> map = new HashMap<>();
                map.put("token", token);
                Cookie tokenCookie = new Cookie("gssq_token", token);
                response.addCookie(tokenCookie);

                log.info("用户 <" + sicnuid + "> 登录成功!");
                return ResultUtils.success(map, "登录成功"); // 登录成功
            }
            return ResultUtils.error("密码错误，请重新输入");
        } else {
            return ResultUtils.error("该学号不存在");
        }

    }

    /**
     * 登出接口
     */
    @GetMapping("/logout")
    public Object logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();//获取 Token
        if (request.getCookies() != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("gssq_token")) { //找到 Token Cookie
                    if (redisUtils.hasKey(c.getValue())) {
                        log.info("用户 uid: " + redisUtils.get(c.getValue()) + " 退出登录...");
                        redisUtils.remove(c.getValue());
                        break;
                    }
                }
            }
        }
        return ResultUtils.success("退出登录完成！");
    }
}
