package com.antzuhl.zeus.dao;

import com.antzuhl.zeus.entity.beans.Namespace;
import com.antzuhl.zeus.entity.beans.Service;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceManager extends BaseManager {

    private static ServiceManager s_instance;

    private ServiceManager() {}

    public static ServiceManager getInstance() {
        if (s_instance != null) {
            return s_instance;
        }
        s_instance = new ServiceManager();
        return s_instance;
    }

    public void addService(Service service) {
        if (service != null) {
            getJdbcTemplate().execute(String.format("INSERT INTO service_registry values ( %d, %d, '%s', '%s', '%s')", service.getId(), service.getNamespaceId(), service.getName(), service.getStatus(), service.getInfo()));
        }
    }

    public void updateService(Service service) {
        if (service != null) {
            getJdbcTemplate().execute(String.format("UPDATE service_registry SET id=%d, namespace='%s', name='%s', status='%s', info='%s' WHERE id = {}", service.getId(), service.getNamespaceId(), service.getName(), service.getStatus(), service.getInfo(), service.getId()));
        }
    }

    public void deleteService(Service service) {
        if (service != null) {
            getJdbcTemplate().execute(String.format("DELETE FROM service_registry WHERE id=%d", service.getId()));
        }
    }

    public List<Service> getAll() {
        return getJdbcTemplate().query("SELECT * FROM service_registry;", new ServiceRowMapper());
    }
}


class ServiceRowMapper implements RowMapper<Service> {
    @Override
    public Service mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Service service = new Service();
        service.setId(resultSet.getInt("id"));
        service.setNamespaceId(resultSet.getInt("namespace_id"));
        service.setName(resultSet.getString("name"));
        service.setStatus(resultSet.getString("status"));
        service.setInfo(resultSet.getString("info"));
        return service;
    }
}