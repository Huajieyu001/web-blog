package top.huajieyu001.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName content
 */
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    /**
     * 文章内容id
     */
    private Integer contentId;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 文章内容索引
     */
    private Integer contentIndex;

    /**
     * 0:未删除 1:已删除
     */
    private Integer isDeleted;

    /**
     * 文章内容id
     */
    public Integer getContentId() {
        return contentId;
    }

    /**
     * 文章内容id
     */
    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    /**
     * 文章内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 文章内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 文章id
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * 文章id
     */
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    /**
     * 文章内容索引
     */
    public Integer getContentIndex() {
        return contentIndex;
    }

    /**
     * 文章内容索引
     */
    public void setContentIndex(Integer contentIndex) {
        this.contentIndex = contentIndex;
    }

    /**
     * 0:未删除 1:已删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 0:未删除 1:已删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Content other = (Content) that;
        return (this.getContentId() == null ? other.getContentId() == null : this.getContentId().equals(other.getContentId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getArticleId() == null ? other.getArticleId() == null : this.getArticleId().equals(other.getArticleId()))
            && (this.getContentIndex() == null ? other.getContentIndex() == null : this.getContentIndex().equals(other.getContentIndex()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getContentId() == null) ? 0 : getContentId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getArticleId() == null) ? 0 : getArticleId().hashCode());
        result = prime * result + ((getContentIndex() == null) ? 0 : getContentIndex().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", contentId=").append(contentId);
        sb.append(", content=").append(content);
        sb.append(", articleId=").append(articleId);
        sb.append(", contentIndex=").append(contentIndex);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append("]");
        return sb.toString();
    }
}