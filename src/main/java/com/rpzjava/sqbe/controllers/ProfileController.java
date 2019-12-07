package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSON;
import com.rpzjava.sqbe.daos.IProfileDAO;
import com.rpzjava.sqbe.entities.UserProfile;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/profile")
public class ProfileController {

    private final IProfileDAO iProfileDAO;

    public ProfileController(IProfileDAO iProfileDAO) {
        this.iProfileDAO = iProfileDAO;
    }

    @GetMapping("/{id}")
    public Object findProfileById(@PathVariable String id) {
        Long uid = Long.parseLong(id);
        Optional<UserProfile> foundProfile = iProfileDAO.findById(uid);
        if (foundProfile.isPresent()) {
            return ResultUtils.success(JSON.toJSON(foundProfile.get()), "获取用户资料成功");
        }
        return ResultUtils.error("没有找到 uid:" + uid + " 用户的资料");
    }

}
