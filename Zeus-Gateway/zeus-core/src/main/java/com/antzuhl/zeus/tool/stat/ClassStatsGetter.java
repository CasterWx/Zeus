package com.antzuhl.zeus.tool.stat;

import com.antzuhl.zeus.tool.common.ClassStats;
import com.antzuhl.zeus.tool.common.Stats;
import com.antzuhl.zeus.tool.common.StatsGetter;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;


public class ClassStatsGetter implements StatsGetter {
    ClassLoadingMXBean bean = ManagementFactory.getClassLoadingMXBean();
    @Override
    public Stats get() {
        ClassStats s = new ClassStats();
        s.setCurrentClassCount(bean.getLoadedClassCount());
        s.setBeenLoadedClassCount(bean.getTotalLoadedClassCount());
        s.setBeenUnloadedClassCount(bean.getUnloadedClassCount());
        return s;
    }
}
