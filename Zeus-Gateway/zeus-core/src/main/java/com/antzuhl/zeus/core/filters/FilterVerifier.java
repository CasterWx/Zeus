package com.antzuhl.zeus.core.filters;

import com.antzuhl.zeus.core.common.Constants;
import com.antzuhl.zeus.core.common.FilterInfo;
import groovy.lang.GroovyClassLoader;

/**
 * verifies that the given source code is compilable in Groovy, can be
 * instanciated, and is a ZeusFilter type
 *
 */
public class FilterVerifier {
	private static final FilterVerifier instance = new FilterVerifier();

	/**
	 * @return Singleton
	 */
	public static FilterVerifier getInstance() {
		return instance;
	}

	/**
	 * verifies compilation, instanciation and that it is a ZeusFilter
	 *
	 * @param sFilterCode
	 * @return a FilterInfo object representing that code
	 * @throws org.codehaus.groovy.control.CompilationFailedException
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public FilterInfo verifyFilter(String sFilterCode) throws org.codehaus.groovy.control.CompilationFailedException,
			IllegalAccessException, InstantiationException {
		Class groovyClass = compileGroovy(sFilterCode);
		Object instance = instanciateClass(groovyClass);
		checkZeusFilterInstance(instance);
		ZeusFilter filter = (ZeusFilter) instance;

		String filter_id = FilterInfo.buildFilterId(Constants.APPLICATION_NAME, filter.filterType(),
				groovyClass.getSimpleName());

		return new FilterInfo(filter_id, sFilterCode, filter.filterType(), groovyClass.getSimpleName(),
				filter.disablePropertyName(), "" + filter.filterOrder(), Constants.APPLICATION_NAME);
	}

	Object instanciateClass(Class groovyClass) throws InstantiationException, IllegalAccessException {
		return groovyClass.newInstance();
	}

	void checkZeusFilterInstance(Object zeusFilter) throws InstantiationException {
		if (!(zeusFilter instanceof ZeusFilter)) {
			throw new InstantiationException("Code is not a ZeusFilter Class ");
		}
	}

	/**
	 * compiles the Groovy source code
	 *
	 * @param sFilterCode
	 * @return
	 * @throws org.codehaus.groovy.control.CompilationFailedException
	 *
	 */
	public Class compileGroovy(String sFilterCode) throws org.codehaus.groovy.control.CompilationFailedException {
		GroovyClassLoader loader = new GroovyClassLoader();
		return loader.parseClass(sFilterCode);
	}

}
