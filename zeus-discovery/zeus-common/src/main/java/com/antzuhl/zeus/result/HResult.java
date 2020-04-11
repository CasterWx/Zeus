package com.antzuhl.zeus.result;

public enum HResult implements IHResult {

    R_OK(0, "请求成功"),
    // 1XXX

    // 2XXX
    R_ALIVE(200, "服务存活")

    // 3XXX
    ;

    private String message;
    private int code;

    HResult(int code, String message) {
        this.message = message;
        this.setCode(code);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getMessage(int code) {
        return valueOf(code).getMessage();
    }


    public static HResult valueOf(int id) {
        for (HResult hr : values()) {
            if (hr.code == id) {
                return hr;
            }
        }
        throw new IllegalArgumentException("No matching HResult for [" + id + "]");
    }

    public static String valueOfString(int id) {
        for (HResult hr : values()) {
            if (hr.code == id) {
                return hr.getMessage();
            }
        }
        throw new IllegalArgumentException("No matching HResult for [" + id + "]");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
