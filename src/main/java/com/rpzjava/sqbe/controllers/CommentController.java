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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static final int PAGE_SIZE = 10;

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
    public Object getCommentsByPostIdWithPage(@PathVariable Long postId, @RequestParam("page") Integer page) {
        Optional<Post> findingPost = iPostDao.findByIdAndStatus(postId, EntityStatus.NORMAL);
        if (!findingPost.isPresent()) {
            return ResultUtils.error("该帖子不存在！", 88404L);
        }
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createTime").descending());
        Page<PostComment> comments = iPostCommentDAO
                .findAllByPostAndStatus(findingPost.get(), EntityStatus.NORMAL, pageable);
        return ResultUtils.success(comments.getContent(), "获取帖子 id: " + postId + "评论成功！");
    }

    @GetMapping("/pagination/{postId}")
    public Object getCommentCountByPostId(@PathVariable Long postId) {
        Optional<Post> findingPost = iPostDao.findByIdAndStatus(postId, EntityStatus.NORMAL);
        if (!findingPost.isPresent()) {
            return ResultUtils.error("该帖子不存在！", 88404L);
        }
        long allCount = iPostCommentDAO.countByPostAndStatus(findingPost.get(), EntityStatus.NORMAL);
        long pagination = allCount / PAGE_SIZE;
        if (allCount % PAGE_SIZE != 0) {
            pagination += 1;
        }
        JSONObject result = new JSONObject();
        result.put("pagination", pagination);
        return ResultUtils.success(result, "成功获取 id: " + postId + " 的帖子的评论页数");
    }

}
