package com.luoyang.hanfu.common;

import lombok.Getter;

/**
 * 业务逻辑异常
 * 
 * @author Backend Architect
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        this(500, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
