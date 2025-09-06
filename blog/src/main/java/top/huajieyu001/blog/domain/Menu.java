package top.huajieyu001.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @TableName title
 */
@Data
public class Menu {
    /**
     * 标题id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题名称
     */
    private String name;

    /**
     * 标题备注
     */
    private String comment;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updateBy;

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