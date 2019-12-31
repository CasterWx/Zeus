package com.antzuhl.zeus.entity.response;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse {
    private String requestId;
    private int code;
    private String error_msg;
    private Object data;
}
