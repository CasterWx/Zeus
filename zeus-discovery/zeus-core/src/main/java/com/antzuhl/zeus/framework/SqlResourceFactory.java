package com.antzuhl.zeus.framework;

import com.antzuhl.zeus.dao.AuditLogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlResourceFactory {

    private boolean flag = true;

    private static SqlSessionFactory sqlSessionFactory;

    private static SqlSession sqlSession;

    private void getSqlSessionFactory() {
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            if (flag)
                createTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SqlSession getSqlSession() {
        if (sqlSessionFactory != null) {
            sqlSession = sqlSessionFactory.openSession();
            return sqlSession;
        } else {
            getSqlSessionFactory();
            return getSqlSession();
        }
    }

    public void close() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    public void createTable() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AuditLogMapper auditLogMapper = sqlSession.getMapper(AuditLogMapper.class);
        auditLogMapper.createTable();
        sqlSession.commit();
        flag = false;
        sqlSession.close();
    }

}
