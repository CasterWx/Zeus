package com.antzuhl.zeus.dao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ZeusBaseDal {

    private static JdbcTemplate jdbcTemplate = JdbcHelper.instance().getJdbcTemplate();

    private static Logger logger = LoggerFactory.getLogger(ZeusBaseDal.class);

    public void createTable() {
        logger.info("Init tables;");
        initNamespaceRegistry();
        initServiceRegistry();
    }

    private void initServiceRegistry() {
        try {
            jdbcTemplate.execute(Constant.SERVICE_REGISTRY);
            logger.info("init server_registry :{}", Constant.SERVICE_REGISTRY);
        } catch (Exception e) {
            logger.info("server_registry is not empty.");
        }
    }

    private void initNamespaceRegistry() {
        try {
            jdbcTemplate.execute(Constant.NAEMSPACE_REGISTRY);
            logger.info("init namespace_registry :{}", Constant.NAEMSPACE_REGISTRY);
        } catch (Exception e) {
            logger.info("namespace_registry is not empty.");
        }
    }

}
