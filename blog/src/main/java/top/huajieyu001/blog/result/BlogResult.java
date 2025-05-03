package top.huajieyu001.blog.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author huajieyu
 * @Date 5/2/2025 9:24 PM
 * @Version 1.0
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogResult {

    private Object data;
    private String message;
    private Integer code;

    public static BlogResult success(String message,Integer code){
        return new BlogResult(null, message,code);
    }

    public static BlogResult success(String message){
        return new BlogResult(null, message,200);
    }

    public static BlogResult success(){
        return new BlogResult(null,null,200);
    }

    public static BlogResult success(Object object){
        return new BlogResult(object,null,200);
    }

    public static BlogResult success(Object object,String message){
        return new BlogResult(object,message,200);
    }

    public static BlogResult error(String message){
        return new BlogResult(null, message,500);
    }

    public static BlogResult error(String message,Integer code){
        return new BlogResult(null, message,code);
    }
}
