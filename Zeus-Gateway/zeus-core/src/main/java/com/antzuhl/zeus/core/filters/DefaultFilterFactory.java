package com.antzuhl.zeus.core.filters;


import com.antzuhl.zeus.core.common.IFilterFactory;

/**
 * Default factory for creating instances of ZeusFilter.
 */
public class DefaultFilterFactory implements IFilterFactory {

    /**
     * Returns a new implementation of ZeusFilter as specified by the provided
     * Class. The Class is instantiated using its nullary constructor.
     * 
     * @param clazz the Class to instantiate
     * @return A new instance of ZeusFilter
     */
    @Override
    public ZeusFilter newInstance(Class clazz) throws InstantiationException, IllegalAccessException {
        return (ZeusFilter) clazz.newInstance();
    }

}