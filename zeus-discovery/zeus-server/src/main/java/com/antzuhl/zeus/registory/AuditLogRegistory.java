package com.antzuhl.zeus.registory;

import com.antzuhl.zeus.beans.AuditLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRegistory extends JpaSpecificationExecutor<AuditLog>, CrudRepository<AuditLog, String> {
}