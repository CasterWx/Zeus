package com.antzuhl.zeus.registory;

import com.antzuhl.zeus.beans.Service;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRegistory extends JpaSpecificationExecutor<Service>, CrudRepository<Service, Long> {
    Service findByServiceName(String serviceName);
}