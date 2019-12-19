package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IPostCommentDAO;
import com.rpzjava.sqbe.daos.IReplyDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.PostComment;
import com.rpzjava.sqbe.entities.Reply;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.tools.EntityStatus;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/reply")
public class ReplyController {

    private final IReplyDAO iReplyDAO;
    private final IUserDAO iUserDAO;
    private final IPostCommentDAO iPostCommentDAO;

    public ReplyController(IReplyDAO iReplyDAO, IUserDAO iUserDAO, IPostCommentDAO iPostCommentDAO) {
        this.iReplyDAO = iReplyDAO;
        this.iUserDAO = iUserDAO;
        this.iPostCommentDAO = iPostCommentDAO;
    }

    @PostMapping("/{commentId}")
    public Object insertNewReply(
            @PathVariable Long commentId,
            @RequestBody JSONObject reqBody,
            @RequestParam("target") String target
    ) {
        Long uid = reqBody.getLong("sender");
        Optional<PostComment> findingComment =
                iPostCommentDAO.findByIdAndStatus(commentId, EntityStatus.NORMAL);
        Optional<UserEntity> findingUser =
                iUserDAO.findByIdAndStatus(uid, EntityStatus.NORMAL);
        if (!findingComment.isPresent()) {
            return ResultUtils.error("该评论不存在", 77404L);
        }
        if (!findingUser.isPresent()) {
            return ResultUtils.error("回复发送用户不存在", 79404L);
        }
        if (target.equals("comment")) {
            String source = reqBody.getString("source");
            String content = reqBody.getString("content");
            Reply reply = new Reply();
            reply.setSender(findingUser.get());
            reply.setSource(source);
            reply.setContent(content);
            reply.setTarget(target);
            reply = iReplyDAO.saveAndFlush(reply);

            Set<Reply> replySet = findingComment.get().getReplySet();
            replySet.add(reply);
            findingComment.get().setReplySet(replySet);
            iPostCommentDAO.saveAndFlush(findingComment.get());
            return ResultUtils.success(reply, "回复成功！");
        } else {
            // TODO: 实现回复 -> 已经存在的回复
            return ResultUtils.success("（没实现）算你回复成功！");
        }
    }

}
