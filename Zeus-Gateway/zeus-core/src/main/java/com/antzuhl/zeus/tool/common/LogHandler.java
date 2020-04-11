package com.antzuhl.zeus.tool.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogHandler implements StatsHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Stats stats) {
        logger.info(stats.toString());
    }
}
