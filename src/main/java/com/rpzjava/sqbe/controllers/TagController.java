package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSON;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/tag")
public class TagController {

    private final IPostDao iPostDao;

    public TagController(IPostDao iPostDao) {
        this.iPostDao = iPostDao;
    }

    @GetMapping("/withPostCount")
    public Object getAllTagsWithPostCount() {
        List<Map<String, Object>> tagMap = iPostDao.countTagByPost();
        return ResultUtils.success(JSON.toJSON(tagMap), "获取标签帖子数成功！");
    }

}
