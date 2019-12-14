package com.rpzjava.sqbe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.EditPostType;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.daos.ITagDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.Post;
import com.rpzjava.sqbe.entities.Tag;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;
import com.rpzjava.sqbe.services.EditPostService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PostUnitTest {

    @Autowired
    ITagDAO iTagDAO;
    @Autowired
    IUserDAO iUserDAO;
    @Autowired
    IPostDao iPostDao;
    @Autowired
    EditPostService editPostService;

    // 此测试已经做过 passed? : yes
    @Test
    void testPostAddTagsNotRepeat() {

        // 此 post 也要存储进数据库
        Post post = new Post();
        try {
            // 测试是 UID 000001 唐梦予发帖
            post.setSender(iUserDAO.findById(1L).orElseThrow(Exception::new));
            post.setTitle("测试帖子");
            post.setSource("# 在这里开始写帖子内容吧！\n快来极客社区和我们一起玩耍吧！");
            post.setContent("<h1><a id=\"_0\"></a>在这里开始写帖子内容吧！</h1>\n<p>快来极客社区和我们一起玩耍吧！</p>\n");

            System.out.println("新建 Post: \n" + post);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        post.setTagSet(new HashSet<>());

        // 新建两个标签
        Tag tag1 = new Tag();
        tag1.setName("算法");
        Tag tag2 = new Tag();
        tag2.setName("Linux");
        // 测试标签是要存进数据库的
        /* 测试报告：[ success ☑️ ]
          此处两个标签不同 不会触发"独一性错误"
          1. 制造重复的标签，无法存进数据库
          2. Set<Tag> 存入的时候比较的是值，即使用 .equals() 方法比较
            而不是根据内存地址比较，如果是比较内存地址一定不一样
         */
        tag1 = iTagDAO.saveAndFlush(tag1);
        tag2 = iTagDAO.saveAndFlush(tag2);

        // 制造一个重复的样例
        Tag tag3 = new Tag();
        tag3.setName("算法");

        // 添加进 post
        post.getTagSet().add(tag1);
        post.getTagSet().add(tag2);
        System.out.println(post.getTagSet());

        // 测试保存 帖子
        iPostDao.saveAndFlush(post);

        try {
            tag3 = iTagDAO.saveAndFlush(tag3);
            post.getTagSet().add(tag3);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
        }

    }

    // 此测试已经做过 passed? : yes
    @Test
    void testGetPostTags() {
        Optional<Post> findingPost = iPostDao.findById(1L);
        findingPost.ifPresent(post -> System.out.println(JSON.toJSONString(post)));
    }

    //  此测试已经做过 passed? : yes
    @Test
    void testSaveDraft() {
        JSONObject fakeReqBody = new JSONObject();
        fakeReqBody.put("uid", 1L);
        fakeReqBody.put("title", "测试保存草稿帖");
        fakeReqBody.put("source", "**打草稿是个好习惯，凡事都要三思而后行。**");
        fakeReqBody.put("content", "<p><strong>打草稿是个好习惯，凡事都要三思而后行。</strong></p>\n");

        JSONArray fakeTags = new JSONArray();
        fakeTags.add("Flutter");
        fakeTags.add("云计算");
        fakeReqBody.put("tags", fakeTags);

        try {
            editPostService.newEdit(fakeReqBody, EditPostType.DRAFT);
        } catch (PostDataNotCompleteException e) {
            System.out.println(e.getMessage());
        }
    }

    // 测试结果：从获取的数据中 通过 FastJSON 解构
    @Test
    void testCountTagByPost() {
        List<Map<String, Object>> tagUsedCounts = iPostDao.countTagByPost();
        System.out.println(JSON.toJSON(tagUsedCounts));
    }

    @Test
    void testGetPostsByTag() {
        List<Post> post = iPostDao.dragPostsByTag("赛哥");
        System.out.println(JSON.toJSON(post));
    }

}
