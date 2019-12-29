package com.antzuhl.zeus.zkutils;

/**
 * ZooKeeper constant
 *
 * @author antzuhl
 */
public interface Constant {

    int ZK_SESSION_TIMEOUT = 15000;

    String OK = "OK.";
    String NOT_FOUND_NODE = "not found node."; //查询空

    int EXPIRE_SECONDS = 86400;
    String ZK_REGISTRY_PATH = "/registry";

    // add NameSpace
}
