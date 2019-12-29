package com.antzuhl.zeus.bean;

import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FlowApiData {
    private String apiName;
    private String classPath;
    private Long call;
}
