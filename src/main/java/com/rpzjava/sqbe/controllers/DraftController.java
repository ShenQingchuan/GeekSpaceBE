package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.tools.EditPostType;
import com.rpzjava.sqbe.tools.ExecuteResult;
import com.rpzjava.sqbe.daos.IDraftDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.Draft;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import com.rpzjava.sqbe.services.EditPostService;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/draft")
@Slf4j
public class DraftController {

    private final EditPostService editPostService;
    private final IDraftDAO iDraftDAO;
    private final IUserDAO iUserDAO;

    public DraftController(
            EditPostService editPostService,
            IDraftDAO iDraftDAO,
            IUserDAO iUserDAO
    ) {
        this.editPostService = editPostService;
        this.iDraftDAO = iDraftDAO;
        this.iUserDAO = iUserDAO;
    }

    @PutMapping("/")
    public Object updateDraft(@RequestBody JSONObject reqBody) {
        try {
            ExecuteResult rewriteResult = editPostService.updateEdit(reqBody, EditPostType.DRAFT);
            if (!rewriteResult.getStatus()) {
                ResultUtils.error("修改草稿失败！" + rewriteResult.getPayload().toString());
            }
        } catch (PostDataNotCompleteException e) {
            return ResultUtils.error("修改草稿失败，提交的数据字段不完整");
        }
        return ResultUtils.success("修改草稿成功！");
    }

    @PostMapping("/")
    public Object saveDraft(@RequestBody JSONObject reqBody) {
        try {
            ExecuteResult newDraftResult = editPostService.newEdit(reqBody, EditPostType.DRAFT);
            if (!newDraftResult.getStatus()) {
                ResultUtils.error("新建草稿失败！" + newDraftResult.getPayload().toString());
            }
        } catch (PostDataNotCompleteException e) {
            return ResultUtils.error("新建草稿失败，提交的数据字段不完整");
        }
        return ResultUtils.success("新建草稿成功！");
    }

    @GetMapping("/{id}")
    public Object getDraftByUid(@PathVariable String id) {
        long uid = Long.parseLong(id);
        Optional<UserEntity> findingUser = iUserDAO.findById(uid);
        if (findingUser.isPresent()) {
            List<Draft> draftList = iDraftDAO.findBySender(findingUser.get());
            if (draftList.size() > 0) {
                return ResultUtils.success(draftList, "获取草稿成功！");
            } else {
                return ResultUtils.success("还没有草稿哟！");
            }
        }

        return ResultUtils.success("获取草稿失败！找不到 uid: " + uid + " 用户的草稿");
    }

    @DeleteMapping("/{draftId}")
    public Object deleteDraftById(@PathVariable String draftId) {
        iDraftDAO.deleteById(Long.parseLong(draftId));
        return ResultUtils.success("删除草稿成功！");
    }

}
