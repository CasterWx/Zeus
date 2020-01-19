package com.antzuhl.zeus.dao;

import com.antzuhl.zeus.dao.utils.JdbcHelper;
import org.springframework.jdbc.core.JdbcTemplate;

public class BaseManager {
    private JdbcTemplate jdbcTemplate = JdbcHelper.instance().getJdbcTemplate();

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
