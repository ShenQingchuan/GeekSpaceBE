package com.rpzjava.sqbe.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.TagFromReqBody;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.daos.ITagDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.pojos.Post;
import com.rpzjava.sqbe.entities.pojos.Tag;
import com.rpzjava.sqbe.entities.pojos.UserEntity;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewPostServiceImpl implements NewPostService {

    private final IUserDAO iUserDAO;
    private final IPostDao iPostDao;
    private final ITagDAO iTagDAO;

    public NewPostServiceImpl(
            IUserDAO iUserDAO,
            IPostDao iPostDao,
            ITagDAO iTagDAO
    ) {
        this.iUserDAO = iUserDAO;
        this.iPostDao = iPostDao;
        this.iTagDAO = iTagDAO;
    }

    /**
     * 新建极客空间论坛帖子
     * @param reqBody 提交新建帖子的请求体
     * @return Boolean 本次新建Service执行成功与否
     * @throws PostDataNotCompleteException 可能请求体字段不全
     */
    @Override
    public Boolean newPost(JSONObject reqBody) throws PostDataNotCompleteException {

        Long uid = reqBody.getLong("uid");
        Optional<UserEntity> findingUser = iUserDAO.findByUid(uid);

        // 本方法的返回值 Boolean 值决定于 此 Optional<UserEntity> 是否为空：
        if (findingUser.isPresent()) {
            String cover = reqBody.getString("cover");
            String title = reqBody.getString("title");
            String content = reqBody.getString("content");
            String source = reqBody.getString("source");
            JSONArray tags = reqBody.getJSONArray("tags");

            if ( cover == null || title == null || content == null || source == null) {
                throw new PostDataNotCompleteException();
            }

            // 设置 post 帖子实体类对象实例的基本字段
            Post post = new Post();
            post.setSender(findingUser.get());
            post.setContent(content);
            post.setTitle(title);
            post.setCoverUrl(cover);
            post.setSource(source);

            // 遍历标签数组
            List<TagFromReqBody> tagList = tags.toJavaList(TagFromReqBody.class);
            tagList.forEach(tag -> {
                Tag newTag = new Tag();
                newTag.setName(tag.getTagName());

                // 若该标签在标签表中找不到，就新建保存它
                // 因为实现查了存在性，所以之后添加不会发生 DataIntegrityViolationException 字段插入重复的错误
                Optional<Tag> findingTag = iTagDAO.findByName(tag.getTagName());
                if (!findingTag.isPresent()) {
                    newTag = iTagDAO.saveAndFlush(newTag);
                }

                post.getTagSet().add(newTag);
            });

            iPostDao.save(post);
            return true;
        }
        return false;
    }
}
