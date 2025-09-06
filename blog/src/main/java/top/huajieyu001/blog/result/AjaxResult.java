package top.huajieyu001.blog.result;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author huajieyu
 */
@Getter
public class AjaxResult {

    private int code;

    private String msg;

    private Object data;

    private AjaxResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static AjaxResult success() {
        return new AjaxResult(HttpStatus.OK.value(), "success", null);
    }

    public static AjaxResult success(Object data) {
        return new AjaxResult(HttpStatus.OK.value(), "success", data);
    }

    public static AjaxResult successAndSetMsg(String msg) {
        return new AjaxResult(HttpStatus.OK.value(), msg, null);
    }

    public static AjaxResult error() {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", null);
    }

    public static AjaxResult error(String msg) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }

    public static AjaxResult success(Object data, String msg) {
        return new AjaxResult(HttpStatus.OK.value(), msg, data);
    }

}