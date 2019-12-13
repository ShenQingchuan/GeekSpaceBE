package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.EditPostType;
import com.rpzjava.sqbe.beans.ExecuteResult;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import com.rpzjava.sqbe.services.EditPostService;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @PutMapping("/")
    public Object updatePost(@RequestBody JSONObject reqBody) {
        try {
            ExecuteResult rewriteResult = editPostService.updateEdit(reqBody, EditPostType.POST);
            if (!rewriteResult.getStatus()) {
                ResultUtils.error("修改帖子失败！" + rewriteResult.getPayload().toString());
            }
        } catch (PostDataNotCompleteException e) {
            return ResultUtils.error("修改帖子失败，提交的数据字段不完整");
        }
        return ResultUtils.success("修改帖子成功！");
    }

    @PostMapping("/")
    public Object savePost(@RequestBody JSONObject reqBody) {

        try {
            ExecuteResult newPostResult = editPostService.newEdit(reqBody, EditPostType.POST);
            if (!newPostResult.getStatus()) {
                ResultUtils.error("发帖失败！" + newPostResult.getPayload().toString());
            }
        } catch (PostDataNotCompleteException e) {
            return ResultUtils.error("发帖失败，提交的数据字段不完整");
        }

        return ResultUtils.success("发帖成功");
    }

    @GetMapping("/latest") //按分页（每页10个）所有帖子数据信息
    public Object getLatestPost(@RequestParam String page) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createTime").descending());
        return ResultUtils.success(
                JSON.toJSON(iPostDao.findAllByStatus(1, pageable).getContent()),
                "获取帖子成功！"
        );
    }

}
