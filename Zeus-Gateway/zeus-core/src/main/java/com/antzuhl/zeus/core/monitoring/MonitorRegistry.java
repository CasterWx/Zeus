package com.antzuhl.zeus.core.monitoring;

import com.antzuhl.zeus.core.common.IMonitor;
import com.antzuhl.zeus.core.common.INamedCount;

public class MonitorRegistry {

    private static  final MonitorRegistry instance = new MonitorRegistry();
    private IMonitor publisher;

    /**
     * A Monitor implementation should be set here
     * @param publisher
     */
    public void setPublisher(IMonitor publisher) {
        this.publisher = publisher;
    }



    public static MonitorRegistry getInstance() {
        return instance;
    }

    public void registerObject(INamedCount monitorObj) {
      if(publisher != null) publisher.register(monitorObj);
    }
}