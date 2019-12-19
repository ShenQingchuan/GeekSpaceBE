package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IPostCommentDAO;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.Post;
import com.rpzjava.sqbe.entities.PostComment;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.tools.EntityStatus;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    private final IPostDao iPostDao;
    private final IPostCommentDAO iPostCommentDAO;
    private final IUserDAO iUserDAO;

    public CommentController(IPostDao iPostDao, IPostCommentDAO iPostCommentDAO, IUserDAO iUserDAO) {
        this.iPostDao = iPostDao;
        this.iPostCommentDAO = iPostCommentDAO;
        this.iUserDAO = iUserDAO;
    }

    @PostMapping("/{postId}")
    public Object insertNewComment(@PathVariable Long postId, @RequestBody JSONObject reqBody) {
        Long sender = reqBody.getLong("sender");
        String source = reqBody.getString("source");
        String content = reqBody.getString("content");

        Optional<Post> findingPost = iPostDao.findByIdAndStatus(postId, EntityStatus.NORMAL);
        Optional<UserEntity> findingUser = iUserDAO.findByIdAndStatus(sender, EntityStatus.NORMAL);
        if (!findingPost.isPresent()) {
            return ResultUtils.error("该帖子不存在！", 88404L);
        }
        if (!findingUser.isPresent()) {
            return ResultUtils.error("该用户不存在！", 99404L);
        }
        PostComment postComment = new PostComment();
        postComment.setPost(findingPost.get());
        postComment.setSender(findingUser.get());
        postComment.setSource(source);
        postComment.setContent(content);

        return ResultUtils.success(iPostCommentDAO.saveAndFlush(postComment), "评论成功！");
    }

    @GetMapping("/{postId}")
    public Object getCommentsByPostId(@PathVariable Long postId) {
        Optional<Post> findingPost = iPostDao.findByIdAndStatus(postId, EntityStatus.NORMAL);
        if (!findingPost.isPresent()) {
            return ResultUtils.error("该帖子不存在！", 88404L);
        }
        List<PostComment> comments = iPostCommentDAO.findByPost(findingPost.get());
        return ResultUtils.success(comments, "获取帖子 id: " + postId +"评论成功！");
    }

}
