package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IDraftDAO;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/draft")
@Slf4j
public class DraftController {

    private final IDraftDAO iDraftDAO;

    public DraftController(IDraftDAO iDraftDAO) {
        this.iDraftDAO = iDraftDAO;
    }

    @PostMapping("/")
    public Object saveDraft(@RequestBody JSONObject reqBody) {
        // TODO: 实现保存草稿（基本原理同 "保存帖子"）
        return ResultUtils.success("保存草稿成功！");
    }

    @PostMapping("/{id}")
    public Object deleteDraft(@RequestBody JSONObject reqBody){
        //TODO：实现删除草稿（可多选）
        return ResultUtils.success("删除成功！");
    }

    @GetMapping("/{id}")
    public Object getDraftByUid(@PathVariable String id) {
        // TODO: 实现按传入的 id（即 uid）查找他的草稿
        return ResultUtils.success("获取草稿成功！");
    }
}
