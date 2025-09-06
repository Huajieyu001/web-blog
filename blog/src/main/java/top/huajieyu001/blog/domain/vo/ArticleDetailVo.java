package top.huajieyu001.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author huajieyu
 * @Date 5/6/2025 11:05 PM
 * @Version 1.0
 * @Description TODO
 */
@Data
public class ArticleDetailVo {

    /**
     * 文章id
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章概述
     */
    private String overview;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 0:未删除 1:已删除
     */
    private Long isDeleted;

    /**
     * 文章内容
     */
    private String content;
}
