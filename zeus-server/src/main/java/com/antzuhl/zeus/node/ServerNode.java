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

    public ServerNode(String nameSpace, String serverName, String data) {
        this.nameSpace = nameSpace;
        this.serverName = serverName;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServerNode{" +
                "nameSpace='" + nameSpace + '\'' +
                ", serverName='" + serverName + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
