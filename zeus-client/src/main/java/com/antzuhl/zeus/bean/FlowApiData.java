package com.antzuhl.zeus.bean;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FlowApiData {
    private String apiName;
    private String classPath;
    private List<Date> dates;
    private Long call;
}
