package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.entities.UserPost;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/release")
@Slf4j
public class ReleaseController {
    private final IPostDao IPostDao;

    public ReleaseController(IPostDao IPostDao) {
        this.IPostDao = IPostDao;
    }

    @PostMapping("/save")
    public Object savePost(@RequestBody JSONObject jsonObject){
        UserPost userPost = new UserPost();

        String sicnuid = jsonObject.get("sicnuid").toString();
        String cover = jsonObject.get("cover").toString();
        String title = jsonObject.get("title").toString();
        String description = jsonObject.get("description").toString();

        userPost.setSicnuid(sicnuid);
        userPost.setDescription(description);
        userPost.setTitle(title);
        userPost.setCoverUrl(cover);

        UserPost savePost = IPostDao.save(userPost);
        if (savePost != null){
            return ResultUtils.success("hold success");
        }
        return ResultUtils.error("hold fail");
    }

    @PostMapping("/display") //返回帖子数据表所有数据信息
    public Object displayPost(){
        return IPostDao.findAll();
    }

}
