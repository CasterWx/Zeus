package com.antzuhl.zeus.core.core;

import com.antzuhl.zeus.core.common.IDynamicCodeCompiler;
import com.antzuhl.zeus.core.common.IFilterFactory;
import com.antzuhl.zeus.core.filters.DefaultFilterFactory;
import com.antzuhl.zeus.core.filters.FilterRegistry;
import com.antzuhl.zeus.core.filters.ZeusFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is one of the core classes in Zeus. It compiles, loads from a File, and checks if source code changed.
 * It also holds ZeusFilters by filterType.
 *
 */
public class FilterLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterLoader.class);

    private final static FilterLoader instance = new FilterLoader();
   
    private static IDynamicCodeCompiler COMPILER;
    private static IFilterFactory FILTER_FACTORY = new DefaultFilterFactory();
    
    private FilterRegistry filterRegistry = FilterRegistry.instance();
    private final ConcurrentHashMap<String, Long> filterClassLastModified = new ConcurrentHashMap<String, Long>();
    private final ConcurrentHashMap<String, String> filterClassCode = new ConcurrentHashMap<String, String>();
    private final ConcurrentHashMap<String, String> filterCheck = new ConcurrentHashMap<String, String>();
    private final ConcurrentHashMap<String, List<ZeusFilter>> hashFiltersByType = new ConcurrentHashMap<String, List<ZeusFilter>>();

    public FilterLoader() {

//    	filterRegistry.put("filter3----", new InternalExecuteRoute());
//    	filterRegistry.put("filter4----", new InternalSearchRoute());
//    	filterRegistry.put("filter0----", new DebugResponse());
//    	filterRegistry.put("filter00----", new DebugRequest());
//    	filterRegistry.put("filter01----", new DebugHeader());
//    	filterRegistry.put("filter02----", new DebugModeSetter());
//    	filterRegistry.put("filter1----", new ErrorResponse());
//    	filterRegistry.put("filter2----", new HealthCheck());
//    	filterRegistry.put("filter5----", new SendResponse());
//    	filterRegistry.put("filter6----", new Stats());
//    	filterRegistry.put("filter7----", new WirelessAddTimeStamp());
//    	filterRegistry.put("filter8----", new WirelessExecuteRoute());
//    	filterRegistry.put("filter9------", new WirelessSearchRouteForAPI());
//    	filterRegistry.put("filter9----", new WirelessSearchRoute());
//    	filterRegistry.put("filter10----", new WirelessValidateHeader());
//    	filterRegistry.put("filter11----", new WirelessValidateTimeStamp());
//    	filterRegistry.put("filter12----", new WirelessValidateToken());

    }
    /**
     * Sets a Dynamic Code Compiler
     *
     * @param compiler
     */
    public void setCompiler(IDynamicCodeCompiler compiler) {
        COMPILER = compiler;
    }

    // overidden by tests
    public void setFilterRegistry(FilterRegistry r) {
        this.filterRegistry = r;
    }

    /**
     * Sets a FilterFactory
     * 
     * @param factory
     */
    public void setFilterFactory(IFilterFactory factory) {
        FILTER_FACTORY = factory;
    }
    
    /**
     * @return Singleton FilterLoader
     */
    public static FilterLoader getInstance() {
        return instance;
    }
    
    /**
     * Given source and name will compile and store the filter if it detects that the filter code has changed or
     * the filter doesn't exist. Otherwise it will return an instance of the requested ZeusFilter
     *
     * @param sCode source code
     * @param sName name of the filter
     * @return the ZeusFilter
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public ZeusFilter getFilter(String sCode, String sName) throws Exception {

        if (filterCheck.get(sName) == null) {
            filterCheck.putIfAbsent(sName, sName);
            if (!sCode.equals(filterClassCode.get(sName))) {
                LOGGER.info("reloading code " + sName);
                filterRegistry.remove(sName);
            }
        }
        ZeusFilter filter = filterRegistry.get(sName);
        if (filter == null) {
            Class clazz = COMPILER.compile(sCode, sName);
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                filter = (ZeusFilter) FILTER_FACTORY.newInstance(clazz);
            }
        }
        return filter;

    }
    
    /**
     * @return the total number of Zeus filters
     */
    public int filterInstanceMapSize() {
        return filterRegistry.size();
    }
    
    /**
     * From a file this will read the ZeusFilter source code, compile it, and add it to the list of current filters
     * a true response means that it was successful.
     *
     * @param file
     * @return true if the filter in file successfully read, compiled, verified and added to Zeus
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    public boolean putFilter(File file) throws Exception {
        String sName = file.getAbsolutePath() + file.getName();
        if (filterClassLastModified.get(sName) != null && (file.lastModified() != filterClassLastModified.get(sName))) {
            LOGGER.debug("reloading filter " + sName);
            filterRegistry.remove(sName);
        }
        ZeusFilter filter = filterRegistry.get(sName);
        if (filter == null) {
            Class clazz = COMPILER.compile(file);
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                filter = (ZeusFilter) FILTER_FACTORY.newInstance(clazz);
                filterRegistry.put(file.getAbsolutePath() + file.getName(), filter);
                filterClassLastModified.put(sName, file.lastModified());
                List<ZeusFilter> list = hashFiltersByType.get(filter.filterType());
                if (list != null) {
                    hashFiltersByType.remove(filter.filterType()); //rebuild this list
                }
                return true;
            }
        }

        return false;
    }    
    
    
    /**
     * Returns a list of filters by the filterType specified
     *
     * @param filterType
     * @return a List<ZeusFilter>
     */
    public List<ZeusFilter> getFiltersByType(String filterType) {

        List<ZeusFilter> list = hashFiltersByType.get(filterType);
        if (list != null) return list;

        list = new ArrayList<ZeusFilter>();

        Collection<ZeusFilter> filters = filterRegistry.getAllFilters();
        for (Iterator<ZeusFilter> iterator = filters.iterator(); iterator.hasNext(); ) {
            ZeusFilter filter = iterator.next();
            if (filter.filterType().equals(filterType)) {
                list.add(filter);
            }
        }
        Collections.sort(list); // sort by priority

        hashFiltersByType.putIfAbsent(filterType, list);
        return list;
    }
}
