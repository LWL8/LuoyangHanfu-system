package com.luoyang.hanfu.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应体
 * 
 * @author Backend Architect
 */
@Data
@Schema(description = "通用结果返回类")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "返回信息")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    private R() {}

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static <T> R<T> error() {
        return error("failure");
    }

    public static <T> R<T> error(String message) {
        return error(500, message);
    }

    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}
