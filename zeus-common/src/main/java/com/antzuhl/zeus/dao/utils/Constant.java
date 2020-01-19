package com.antzuhl.zeus.dao.utils;

public class Constant {

    /**
     * 服务注册
     * colum id 服务id
     * colum namespace 集群名 default 'public'
     * colum service 服务名
     * colum status 服务状态 UP or DOWN
     * colum info 描述信息
     * */
    public static String SERVICE_REGISTRY = "CREATE TABLE service_registry(" +
            "id INTEGER PRIMARY KEY," +
            "namespace_id INTEGER NOT NULL," +
            "name VARCHAR(256) NOT NULL," +
            "status VARCHAR(20)," +
            "info MEDIUMTEXT);";

    /**
     * 集群注册
     * colum id 集群id
     * colum namespace 集群名
     * */
    public static String NAEMSPACE_REGISTRY = "CREATE TABLE namespace_registry(" +
            "id INTEGER PRIMARY KEY," +
            "name VARCHAR(128) NOT NULL UNIQUE," +
            "info VARCHAR(256) DEFAULT '');";

    /*
     * 配置中心
     * colum id 编号
     * colum name 配置名
     * colum value 配置值
     * */
    public static String SERVICE_CONFIG = "CREATE TABLE service_config(" +
            "id INTEGER PRIMARY KEY," +
            "name VARCHAR(200) NOT NULL," +
            "value mediumtext DEFAULT '');";

    /**
     * RPC注冊中心
     * colum id
     * colum service_id 服务id
     * colum service 服务地址
     * colum class 注册类信息
     * */
    public static String REMOTE_CALL = "CREATE TABLE remote_call(" +
            "id INTEGER PRIMARY KEY," +
            "service_id INTEGER NOT NULL," +
            "service VARCHAR(128) NOT NULL," +
            "class VARCHAR(256) NOT NULL);";

}
