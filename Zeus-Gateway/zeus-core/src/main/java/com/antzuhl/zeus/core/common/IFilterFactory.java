package com.antzuhl.zeus.core.common;

import com.antzuhl.zeus.core.filters.ZeusFilter;

/**
 * Interface to provide instances of ZeusFilter from a given class.
 */
public interface IFilterFactory {

	/**
	 * Returns an instance of the specified class.
	 * 
	 * @param clazz
	 *            the Class to instantiate
	 * @return an instance of ZeusFilter
	 * @throws Exception
	 *             if an error occurs
	 */
	public ZeusFilter newInstance(Class clazz) throws Exception;
}