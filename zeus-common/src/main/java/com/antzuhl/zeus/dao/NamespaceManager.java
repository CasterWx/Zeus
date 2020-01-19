package com.antzuhl.zeus.dao;

import com.antzuhl.zeus.entity.beans.Namespace;
import com.sun.org.apache.xml.internal.utils.NameSpace;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NamespaceManager extends BaseManager {

    private static NamespaceManager s_instance;

    private NamespaceManager() {
    }

    public static NamespaceManager getInstance() {
        if (s_instance != null) {
            return s_instance;
        }
        s_instance = new NamespaceManager();
        return s_instance;
    }

    public void addNamespace(Namespace namespace) {
        if (namespace != null) {
            getJdbcTemplate().execute(String.format("INSERT INTO namespace_registry values (%d, '%s', '%s')", namespace.getId(), namespace.getName(), namespace.getInfo()));
        }
    }

    public void updateNamespace(Namespace namespace) {
        if (namespace != null) {
            getJdbcTemplate().execute(String.format("UPDATE namespace_registry SET id=%d, name='%s', info='%s' WHERE id =%d", namespace.getId(), namespace.getName(), namespace.getInfo(), namespace.getId()));
        }
    }

    public void deleteNamespace(Namespace namespace) {
        if (namespace != null) {
            getJdbcTemplate().execute(String.format("DELETE FROM namespace_registry WHERE id=%d", namespace.getId()));
        }
    }

    public List<Namespace> getAll() {
        return getJdbcTemplate().query("SELECT * FROM namespace_registry;", new NamespaceRowMapper());
    }
}

class NamespaceRowMapper implements RowMapper<Namespace> {
    @Override
    public Namespace mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Namespace namespace = new Namespace();
        namespace.setId(resultSet.getInt("id"));
        namespace.setName(resultSet.getString("name"));
        namespace.setInfo(resultSet.getString("info"));
        return namespace;
    }
}