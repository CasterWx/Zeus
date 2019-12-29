package com.antzuhl.zeus.node;

import com.antzuhl.zeus.zkutils.Constant;
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

    public String getPath() {
        return Constant.ZK_REGISTRY_PATH+"/"+nameSpace+"/"+serverName;
    }
}
