package com.antzuhl.zeus.entity.beans;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlowApiLog {
    private Long id;
    private String name;
    private Date date;
}
