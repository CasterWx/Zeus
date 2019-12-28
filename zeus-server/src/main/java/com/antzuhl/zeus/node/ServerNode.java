package com.antzuhl.zeus.node;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerNode {
    private String nameSpace;
    private String serverName;
    private String data;
}
