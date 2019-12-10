package com.rpzjava.sqbe.services.impls;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.EditPostType;
import com.rpzjava.sqbe.daos.IDraftDAO;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.daos.ITagDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.pojos.*;
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
     * 执行编辑帖子保存
     *
     * @param type    编辑帖子的类型（保存并发表 or 保存草稿）
     * @param sender  帖子发送用户
     * @param title   帖子标题
     * @param cover   帖子封面图网络地址
     * @param tags    帖子标签JSON数组
     * @param source  帖子markdown源内容
     * @param content 帖子html渲染结果
     */
    private boolean saveEditing(
            EditPostType type,
            UserEntity sender,
            String title,
            String cover,
            JSONArray tags,
            String source,
            String content
    ) {
        boolean createResult = false;
        try {
            // 设置 post 帖子实体类对象实例的基本字段
            PostBase postBase = type == EditPostType.POST ? new Post() : new Draft();
            postBase.setSender(sender);
            postBase.setContent(content);
            postBase.setTitle(title);
            postBase.setCoverUrl(cover);
            postBase.setSource(source);

            // 遍历标签数组
            System.out.println(tags.toJSONString());
            List<String> tagList = tags.toJavaList(String.class);
            tagList.forEach(tag -> {


                // 若该标签在标签表中找不到，就新建保存它
                // 因为实现查了存在性，所以之后添加不会发生 DataIntegrityViolationException 字段插入重复的错误
                Optional<Tag> findingTag = iTagDAO.findByName(tag);
                if (!findingTag.isPresent()) {
                    Tag newTag = new Tag();
                    newTag.setName(tag);
                    iTagDAO.saveAndFlush(newTag);
                } else {
                    // 根据编辑帖子的类型（是直接发表？还是编辑草稿？）
                    // 添加帖子的标签
                    if (type == EditPostType.POST) {
                        ((Post) postBase).getTagSet().add(findingTag.get());
                    } else {
                        ((Draft) postBase).getTagSet().add(findingTag.get());
                    }
                }
            });

            if (type == EditPostType.POST) {
                iPostDao.saveAndFlush((Post) postBase);
            } else {
                iDraftDAO.saveAndFlush((Draft) postBase);
            }
            createResult = true;

        } catch (Exception e) {
            // 发生任何错误 全剧拦截并返回 false
            e.printStackTrace();
        }

        return createResult;
    }

    /**
     * 新建极客空间论坛帖子的编辑过程
     *
     * @param reqBody 提交新建帖子的请求体
     * @return Boolean 本次新建服务 执行成功与否
     * @throws PostDataNotCompleteException 可能请求体字段不全
     */
    @Override
    public boolean newEdit(JSONObject reqBody, EditPostType type) throws PostDataNotCompleteException {

        boolean editResult = false;

        Long uid = reqBody.getLong("uid");
        Optional<UserEntity> findingUser = iUserDAO.findByUid(uid);

        // 本方法的返回值 Boolean 值决定于 此 Optional<UserEntity> 是否为空：
        if (findingUser.isPresent()) {
            String title = reqBody.getString("title");
            String cover = reqBody.getString("cover");
            JSONArray tags = reqBody.getJSONArray("tags");
            String source = reqBody.getString("source");
            String content = reqBody.getString("content");

            // 验证此四样条件，确认必填字段完整
            if (title != null && content != null && source != null && tags.size() > 0) {
                editResult = saveEditing(type, findingUser.get(), title, cover, tags, source, content);
            } else throw new PostDataNotCompleteException();

        }

        return editResult;
    }
}
