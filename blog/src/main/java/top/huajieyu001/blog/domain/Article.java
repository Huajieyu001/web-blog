package top.huajieyu001.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 
 * @TableName article
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article {
    /**
     * 文章id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章类别id
     */
    private Long menuId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章概述
     */
    private String summary;

    /**
     * 文章概述
     */
    private String content;

    /**
     * 文章概述
     */
    private String articleNo;

    /**
     * 文章概述
     */
    private Integer version;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;

    /**
     * 0:未删除 1:已删除
     */
    private Integer isDeleted;
}