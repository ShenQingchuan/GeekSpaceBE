package com.rpzjava.sqbe;

import com.alibaba.fastjson.JSON;
import com.rpzjava.sqbe.daos.IPostDao;
import com.rpzjava.sqbe.daos.ITagDAO;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.pojos.Post;
import com.rpzjava.sqbe.entities.pojos.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
class SqbeApplicationTests {

    @Autowired
    ITagDAO iTagDAO;
    @Autowired
    IUserDAO iUserDAO;
    @Autowired
    IPostDao iPostDao;

    @Deprecated  // 此测试已经做过
    @Test
    void testPostAddTagsNotRepeat() {

        // 此 post 也要存储进数据库
        Post post = new Post();
        try {
            // 测试是 UID 000001 唐梦予发帖
            post.setSender(iUserDAO.findByUid(1L).orElseThrow(Exception::new));
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
        tag1.setName("云计算");
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
        tag3.setName("云计算");

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

    @Test void testGetPostTags() {
        Optional<Post> findingPost = iPostDao.findById(1L);
        findingPost.ifPresent(post -> System.out.println(JSON.toJSONString(post)));
    }

}
