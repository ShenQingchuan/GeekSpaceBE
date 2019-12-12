package com.rpzjava.sqbe.services.impls;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.EditPostType;
import com.rpzjava.sqbe.beans.ExecuteResult;
import com.rpzjava.sqbe.daos.IDraftDAO;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.daos.ITagDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.*;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import com.rpzjava.sqbe.services.EditPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EditPostServiceImpl implements EditPostService {

    private final IUserDAO iUserDAO;
    private final IPostDao iPostDao;
    private final ITagDAO iTagDAO;
    private final IDraftDAO iDraftDAO;

    public EditPostServiceImpl(
            IUserDAO iUserDAO,
            IPostDao iPostDao,
            ITagDAO iTagDAO,
            IDraftDAO iDraftDAO
    ) {
        this.iUserDAO = iUserDAO;
        this.iPostDao = iPostDao;
        this.iTagDAO = iTagDAO;
        this.iDraftDAO = iDraftDAO;
    }

    /**
     * 新建极客空间论坛帖子的编辑过程
     *
     * @param reqBody 提交新建帖子的请求体
     * @return Boolean 本次新建服务 执行成功与否
     * @throws PostDataNotCompleteException 可能请求体字段不全
     */
    @Override
    public ExecuteResult newEdit(JSONObject reqBody, EditPostType type) throws PostDataNotCompleteException {

        ExecuteResult editResult = new ExecuteResult();
        Long uid = reqBody.getLong("uid");
        Optional<UserEntity> findingUser = iUserDAO.findById(uid);

        // 提交用户不存在则直接拒绝
        if (findingUser.isPresent()) {
            String title = reqBody.getString("title");
            JSONArray tags = reqBody.getJSONArray("tags");
            String source = reqBody.getString("source");
            String content = reqBody.getString("content");

            // 验证此四样条件，确认必填字段完整
            if (title != null && content != null && source != null && tags.size() > 0) {
                String cover = reqBody.getString("cover");

                ExecuteResult saveResult = saveNewEditing(reqBody, type, findingUser.get(), title, cover, tags, source, content);
                editResult.setStatus(saveResult.getStatus());
                editResult.setPayload(saveResult.getPayload());
            } else throw new PostDataNotCompleteException();

        } else {
            editResult.setStatus(false);
            editResult.setPayload("没有找到该用户 (uid: " + uid + ")");
        }

        return editResult;
    }

    @Override
    public ExecuteResult updateEdit(JSONObject reqBody, EditPostType type) throws PostDataNotCompleteException {

        ExecuteResult rewriteResult = new ExecuteResult();
        Long uid = reqBody.getLong("uid");
        Optional<UserEntity> findingUser = iUserDAO.findById(uid);

        // 提交用户不存在则直接拒绝
        if (findingUser.isPresent()) {
            String title = reqBody.getString("title");
            JSONArray tags = reqBody.getJSONArray("tags");
            String source = reqBody.getString("source");
            String content = reqBody.getString("content");

            // 验证此四样条件，确认必填字段完整
            if (title != null && content != null && source != null && tags.size() > 0) {

                // 此处还并不能确认是 post 还是 draft 的 id
                Long id = reqBody.getLong("id");
                String cover = reqBody.getString("cover");
                if (type == EditPostType.POST) {
                    Optional<Post> findingPost = iPostDao.findById(id);
                    if (findingPost.isPresent()) {
                        Post rePost = findingPost.get();

                        // TODO: 发布者修改已经发布过的帖子
                        rewriteResult.setStatus(true);
                    }
                }
                else {
                    Optional<Draft> findingDraft = iDraftDAO.findById(id);
                    if (findingDraft.isPresent()) {
                        Draft reDraft = findingDraft.get();
                        reDraft.setTitle(title);
                        reDraft.setCoverUrl(cover);
                        reDraft.setSource(source);
                        reDraft.setContent(content);

                        // 遍历标签数组
                        List<String> tagList = tags.toJavaList(String.class);
                        tagList.forEach(tag -> {
                            // 若该标签在标签表中找不到，就新建保存它
                            // 因为实现查了存在性，所以之后添加不会发生 DataIntegrityViolationException 字段插入重复的错误
                            Optional<Tag> findingTag = iTagDAO.findByName(tag);
                            if (!findingTag.isPresent()) {
                                Tag newTag = new Tag();
                                newTag.setName(tag);
                                iTagDAO.saveAndFlush(newTag);
                                reDraft.getTagSet().add(newTag);
                            } else {
                                reDraft.getTagSet().add(findingTag.get());
                            }
                        });

                        // 保存更新后的草稿
                        iDraftDAO.saveAndFlush(reDraft);
                        rewriteResult.setStatus(true);
                    }
                }
            }
            else throw new PostDataNotCompleteException();
        }
        else {
            rewriteResult.setStatus(false);
            rewriteResult.setPayload("发帖用户UID不存在。");
        }

        return rewriteResult;
    }

    /**
     * 执行编辑帖子保存
     *
     * @param reqBody 为了获取可能的：从草稿继续编辑后完成的帖子，保存帖子则自动删除草稿
     * @param type    编辑帖子的类型（保存并发表 or 保存草稿）
     * @param sender  帖子发送用户
     * @param title   帖子标题
     * @param cover   帖子封面图网络地址
     * @param tags    帖子标签JSON数组
     * @param source  帖子markdown源内容
     * @param content 帖子html渲染结果
     */
    private ExecuteResult saveNewEditing(
            JSONObject reqBody, EditPostType type, UserEntity sender, String title,
            String cover, JSONArray tags, String source, String content
    ) {
        ExecuteResult saveResult = new ExecuteResult();
        try {
            // 设置 post 帖子实体类对象实例的基本字段
            PostBase postBase = type == EditPostType.POST ? new Post() : new Draft();
            postBase.setSender(sender);
            postBase.setContent(content);
            postBase.setTitle(title);
            postBase.setCoverUrl(cover);
            postBase.setSource(source);

            // 遍历标签数组
            List<String> tagList = tags.toJavaList(String.class);
            tagList.forEach(tag -> {
                // 若该标签在标签表中找不到，就新建保存它
                // 因为实现查了存在性，所以之后添加不会发生 DataIntegrityViolationException 字段插入重复的错误
                Optional<Tag> findingTag = iTagDAO.findByName(tag);
                if (!findingTag.isPresent()) {
                    Tag newTag = new Tag();
                    newTag.setName(tag);
                    iTagDAO.saveAndFlush(newTag);
                    if (type == EditPostType.POST) {
                        ((Post) postBase).getTagSet().add(newTag);
                    } else {
                        ((Draft) postBase).getTagSet().add(newTag);
                    }
                }
                else {
                    if (type == EditPostType.POST) {
                        ((Post) postBase).getTagSet().add(findingTag.get());
                    } else {
                        ((Draft) postBase).getTagSet().add(findingTag.get());
                    }
                }
            });

            if (type == EditPostType.POST) {
                iPostDao.saveAndFlush((Post) postBase);

                // 若是从草稿继续编辑完成的，根据提交的草稿号删除草稿
                Long fromDraftId = reqBody.getLong("draft");
                if (fromDraftId != null && iDraftDAO.findById(fromDraftId).isPresent()) {
                    iDraftDAO.deleteById(fromDraftId);
                }
            } else {
                iDraftDAO.saveAndFlush((Draft) postBase);
            }
            saveResult.setStatus(true);

        } catch (Exception e) {
            // 发生任何错误 全剧拦截并返回 false
            e.printStackTrace();
            saveResult.setStatus(false);
            saveResult.setPayload(e.toString());
        }

        return saveResult;
    }


}
