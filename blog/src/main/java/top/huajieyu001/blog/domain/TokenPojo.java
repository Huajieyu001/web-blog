package top.huajieyu001.blog.domain;

import lombok.Data;

/**
 * @TableName account
 */
@Data
public class TokenPojo extends Account {
    private String token;
}