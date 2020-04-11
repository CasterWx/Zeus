package com.antzuhl.zeus.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Getter
@Setter
public class AuditLog {

    private Long id;
    private String logId;
    private Date createTime;
    private Date modifyTime;
    private String method;
    private String path;
    private Integer living;
    private String useTime;
    private Integer status;
}
