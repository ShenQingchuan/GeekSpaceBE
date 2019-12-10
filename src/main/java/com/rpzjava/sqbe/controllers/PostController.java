package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.EditPostType;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import com.rpzjava.sqbe.services.EditPostService;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    private final IPostDao iPostDao;
    private final EditPostService editPostService;

    public PostController(
            IPostDao iPostDao, EditPostService editPostService) {
        this.iPostDao = iPostDao;
        this.editPostService = editPostService;
    }

    @PostMapping("/")
    public Object savePost(@RequestBody JSONObject reqBody) {

        try {
            if (!editPostService.newEdit(reqBody, EditPostType.POST)) {
                ResultUtils.error("发帖失败，发帖用户UID不存在！");
            }
        } catch (PostDataNotCompleteException e) {
            return ResultUtils.error("发帖失败，提交的数据字段不完整");
        }

        return ResultUtils.success("发帖成功");
    }

    @GetMapping("/") //按分页（每页10个）所有帖子数据信息
    public Object getPost(@RequestParam String page) {
        Pageable pageable = (Pageable) PageRequest.of(Integer.parseInt(page), 10);
        return ResultUtils.success(
                JSON.toJSON(iPostDao.findAll(pageable)),
                "获取帖子成功！"
        );
    }

}
