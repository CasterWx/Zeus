package com.antzuhl.zeus.dao;

import com.antzuhl.zeus.framework.SqlResourceFactory;
import org.apache.ibatis.session.SqlSession;

import java.sql.SQLException;

public class MapperFactory {

    private static SqlSession sqlSession = null;

    static {
        sqlSession = new SqlResourceFactory().getSqlSession();
        try {
            sqlSession.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static AuditLogMapper auditLogMapper = sqlSession.getMapper(AuditLogMapper.class);
}
