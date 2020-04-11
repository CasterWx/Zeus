package com.antzuhl.zeus.core.common;

import com.antzuhl.zeus.core.filters.ZeusFilter;

/**
 * Interface to implement for registering a callback for each time a filter
 * is used.
 *
 */
public interface IFilterUsageNotifier {
    public void notify(ZeusFilter filter, ExecutionStatus status);
}
