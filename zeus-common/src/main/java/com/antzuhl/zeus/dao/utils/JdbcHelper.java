package com.antzuhl.zeus.dao.utils;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcHelper {
    private static JdbcHelper s_instance = null;
    private DataSource ds = null ;
    private JdbcTemplate jdbcTemplate ;

    public JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(getDataSource());
        }
        return jdbcTemplate;
    }

    private JdbcHelper() {
    }

    public static JdbcHelper instance() {
        if (s_instance == null) {
            s_instance = new JdbcHelper();
        }

        return s_instance;
    }

    protected DataSource getDataSource() {
        if (ds == null) {
            DataSourceProperties dataSourceProperties = new DataSourceProperties();
            dataSourceProperties.setUrl("jdbc:sqlite:" + System.getProperties().getProperty("user.home") + "\\zeus.db");
            dataSourceProperties.setDriverClassName("org.sqlite.JDBC");
            ds = dataSourceProperties.initializeDataSourceBuilder().build();
        }
        return ds;
    }


}
