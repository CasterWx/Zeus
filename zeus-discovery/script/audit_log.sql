    CREATE TABLE IF NOT EXISTS `audit_log`
    (
      id VARCHAR (255) PRIMARY KEY COMMENT "日志id, 服务名称-0000000000",
      create_time timestamp(0) NOT NULL COMMENT "日期",
      modify_time timestamp(0) NOT NULL COMMENT "日期",
      method varchar (255) COMMENT "请求方法",
      path varchar(255) COMMENT "请求路径",
      living INTEGER (255) COMMENT "是否还没有被收集，1:未收集，0:已收集",
      status INTEGER (255) COMMENT "状态"
    )