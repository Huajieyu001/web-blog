package top.huajieyu001.blog.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 
 * @TableName account
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    /**
     * 账号id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 昵称
     */
    private String alias;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    public String email;

    /**
     * 1:管理员权限 2:高级用户权限 3:用户权限 4:游客权限
     */
    public Integer permissions;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 0:未删除 1:已删除
     */
    private Integer isDeleted;
}