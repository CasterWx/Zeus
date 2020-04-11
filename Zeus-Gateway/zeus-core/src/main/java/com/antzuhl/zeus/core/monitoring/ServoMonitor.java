package com.antzuhl.zeus.core.monitoring;

import com.antzuhl.zeus.core.common.IMonitor;
import com.antzuhl.zeus.core.common.INamedCount;
import com.netflix.servo.monitor.Monitors;

public class ServoMonitor implements IMonitor {
    @Override
    public void register(INamedCount monitorObj) {
        Monitors.registerObject(monitorObj);
    }
}