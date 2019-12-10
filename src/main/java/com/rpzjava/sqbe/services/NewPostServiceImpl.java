package com.rpzjava.sqbe.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.TagFromReqBody;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.daos.IPostTagsRelDAO;
import com.rpzjava.sqbe.daos.ITagDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.pojos.Post;
import com.rpzjava.sqbe.entities.pojos.Tag;
import com.rpzjava.sqbe.entities.pojos.UserEntity;
import com.rpzjava.sqbe.entities.relationships.PostTagsRel;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewPostServiceImpl implements NewPostService {

    private final IUserDAO iUserDAO;
    private final IPostDao iPostDao;
    private final ITagDAO iTagDAO;
    private final IPostTagsRelDAO iPostTagsRelDAO;

    public NewPostServiceImpl(
            IUserDAO iUserDAO,
            IPostDao iPostDao,
            ITagDAO iTagDAO, IPostTagsRelDAO iPostTagsRelDAO
    ) {
        this.iUserDAO = iUserDAO;
        this.iPostDao = iPostDao;
        this.iTagDAO = iTagDAO;
        this.iPostTagsRelDAO = iPostTagsRelDAO;
    }

    @Override
    public Boolean newPost(JSONObject reqBody, Long uid) throws PostDataNotCompleteException {

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
                Optional<Tag> findingTag = iTagDAO.findByName(tag.getTagName());
                if (!findingTag.isPresent()) {
                    iTagDAO.save(newTag);
                }

                // 保存新的 帖子与标签的关系
                PostTagsRel newPostTagsRel = new PostTagsRel();
                newPostTagsRel.setPostId(post.getId());
                newPostTagsRel.setTagId(newTag.getId());
                iPostTagsRelDAO.save(newPostTagsRel);
            });

            iPostDao.save(post);
            return true;
        }
        return false;
    }
}
