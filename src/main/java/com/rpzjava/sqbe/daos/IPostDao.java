package com.rpzjava.sqbe.daos;

import com.rpzjava.sqbe.tools.EntityStatus;
import com.rpzjava.sqbe.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPostDao extends JpaRepository<Post, Long> {

    /**
     * 通过 数据库id主键获取帖子
     * @param pid 帖子主键id
     * @return Optional包装的Post，作安全保障
     */
    @Override
    Optional<Post> findById(Long pid);

    Optional<Post> findByIdAndStatus(Long id, EntityStatus status);

    @Query("select p from Post p join p.tagSet t where t.name = :name")
    List<Post> dragPostsByTag(String name);

    /**
     * 根据状态来查询帖子，返回结果集
     * @param status 具体请看 beans.EntityStatus
     * @param pageable 分页对象
     * @return 被分页化的帖子结果集
     */
    Page<Post> findAllByStatus(EntityStatus status, Pageable pageable);

    /**
     * 查询标签的引用量（该标签下有多少帖子）用于标签云
     * @return 返回一个 元素为 { id, name, "count" } 的结果集
     */
    @Query(value = "select id, `name`, count(rel.tag_id) as \"count\" " +
            "from sqbe_tags as t LEFT JOIN sqbe_rel_post_tags as rel " +
            "on t.id=rel.tag_id GROUP BY t.id", nativeQuery = true)
    List<Map<String, Object>> countTagByPost();

    /**
     * 根据帖子主键id **软删除**
     * @param id 帖子主键id
     */
    @Override
    @Query("update Post set status = 0 where id = :id")
    void deleteById(Long id);

    @Override
    long count();
}
