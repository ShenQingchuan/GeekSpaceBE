package com.rpzjava.sqbe.services.impls;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IPostCommentDAO;
import com.rpzjava.sqbe.daos.IReplyDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.PostComment;
import com.rpzjava.sqbe.entities.Reply;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.services.ReplyService;
import com.rpzjava.sqbe.tools.EntityStatus;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    private final IReplyDAO iReplyDAO;
    private final IPostCommentDAO iPostCommentDAO;
    private final IUserDAO iUserDAO;

    public ReplyServiceImpl(IReplyDAO iReplyDAO, IPostCommentDAO iPostCommentDAO, IUserDAO iUserDAO) {
        this.iReplyDAO = iReplyDAO;
        this.iPostCommentDAO = iPostCommentDAO;
        this.iUserDAO = iUserDAO;
    }

    /**
     * 添加新的回复
     * @param commentId     回复所属评论的
     * @param reqBody       回复的 POST HTTP 请求体
     * @param targetType    回复的类型：'to comment' or 'to reply'
     * @return              返回 ResultUtils.success or .error
     */
    @Override
    public Object newReply(Long commentId, JSONObject reqBody, String targetType) {
        Long uid = reqBody.getLong("sender");
        Optional<PostComment> findingComment = iPostCommentDAO.findByIdAndStatus(commentId, EntityStatus.NORMAL);
        Optional<UserEntity> findingUser = iUserDAO.findByIdAndStatus(uid, EntityStatus.NORMAL);
        if (!findingComment.isPresent()) {
            return ResultUtils.error("该评论不存在", 77404L);
        }
        if (!findingUser.isPresent()) {
            return ResultUtils.error("回复发送用户不存在", 79404L);
        }

        // 创建新的回复记录
        String source = reqBody.getString("source");
        String content = reqBody.getString("content");
        Reply reply = new Reply();
        reply.setSender(findingUser.get());
        reply.setSource(source);
        reply.setContent(content);
        reply.setTarget(targetType);
        reply = iReplyDAO.saveAndFlush(reply);
        Set<Reply> replySet = findingComment.get().getReplySet();
        replySet.add(reply);
        findingComment.get().setReplySet(replySet);
        iPostCommentDAO.saveAndFlush(findingComment.get());

        // 若是 reply to reply 则还要保存 @提到了回复目标项的发送者
        if (targetType.equals("comment")) { // reply to comment
            return ResultUtils.success(reply, "回复成功！");
        } else {    // reply to reply
            Long targetSenderUid = reqBody.getJSONObject("to")
                    .getJSONObject("sender")
                    .getJSONObject("userProfile")
                    .getLong("id");
            Optional<UserEntity> findingTargetSender = iUserDAO.findByIdAndStatus(targetSenderUid, EntityStatus.NORMAL);
            if (!findingTargetSender.isPresent()) {
                return ResultUtils.error("回复发送用户不存在", 79404L);
            }
            reply.setMention(findingTargetSender.get());
            reply = iReplyDAO.saveAndFlush(reply);
            return ResultUtils.success(reply,
                    "回复用户 " + findingTargetSender.get().getUserProfile().getNickName() + "成功！"
            );
        }
    }
}
