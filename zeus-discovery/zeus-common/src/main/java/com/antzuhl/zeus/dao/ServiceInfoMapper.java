package com.antzuhl.zeus.dao;

import com.antzuhl.zeus.entity.ServiceInfo;

public interface ServiceInfoMapper {
    int deleteByPrimaryKey(Integer serviceId);

    int insert(ServiceInfo record);

    int insertSelective(ServiceInfo record);

    ServiceInfo selectByPrimaryKey(Integer serviceId);

    int updateByPrimaryKeySelective(ServiceInfo record);

    int updateByPrimaryKey(ServiceInfo record);
}