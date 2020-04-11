package com.antzuhl.zeus.core.common;

public interface IMonitor {
    /**
     * Implement this to add this Counter to a Registry
     * @param monitorObj
     */
    void register(INamedCount monitorObj);
}
