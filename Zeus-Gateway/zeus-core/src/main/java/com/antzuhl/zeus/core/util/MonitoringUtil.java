package com.antzuhl.zeus.core.util;

import com.antzuhl.zeus.core.common.ITracer;
import com.antzuhl.zeus.core.monitoring.CounterFactory;
import com.antzuhl.zeus.core.monitoring.TracerFactory;

/**
 * Dummy implementations of CounterFactory, TracerFactory, and Tracer
 */
public class MonitoringUtil {

    public static final void initMocks() {
        CounterFactory.initialize(new CounterFactoryImpl());
        TracerFactory.initialize(new TracerFactoryImpl());
    }

    private static final class CounterFactoryImpl extends CounterFactory {
        @Override
        public void increment(String name) {}
    }

    private static final class TracerFactoryImpl extends TracerFactory {
        @Override
        public ITracer startMicroTracer(String name) {
            return new TracerImpl();
        }
    }

    private static final class TracerImpl implements ITracer {
        @Override
        public void setName(String name) {}

        @Override
        public void stopAndLog() {}
    }

}