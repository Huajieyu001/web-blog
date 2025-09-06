package top.huajieyu001.blog.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssConfig {
    /**
     * 端点
     */
    private String endpoint;
    /**
     * Bucket 名称
     */
    private String bucket;
    /**
     * 指定上传到 OSS 的文件前缀
     */
    private String dir;
    /**
     * 指定过期时间，单位为秒
     */
    private long expireTime = 3600;
    /**
     * 构造 host
     */
    private String host;
    /**
     * 认证 accessKeyId
     */
    private String accessKeyId;
    /**
     * 认证 accessKeySecret
     */
    private String accessKeySecret;

    private OSS ossClient;

    @Bean
    public OSS getOssClient() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }

    @PreDestroy
    public void onDestroy() {
        ossClient.shutdown();
    }
}