package com.antzuhl.zeus.entity.response;

import lombok.*;

@Getter
@Setter
public class RpcResponse {
    private String requestId;
    private int code;
    private String error_msg;
    private Object data;

    public RpcResponse() {}

    public RpcResponse(String requestId, int code, String error_msg, Object data) {
        this.requestId = requestId;
        this.code = code;
        this.error_msg = error_msg;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
