package com.antzuhl.zeus.tool.servlet;

import com.alibaba.fastjson.JSON;
import com.antzuhl.zeus.tool.common.StatusInfo;
import com.antzuhl.zeus.tool.stat.ClassStatsGetter;
import com.antzuhl.zeus.tool.stat.MemoryStatsGetter;
import com.antzuhl.zeus.tool.stat.OperatingSystemStatsGetter;
import com.antzuhl.zeus.tool.stat.ThreadStatsGetter;
import com.antzuhl.zeus.tool.util.TimeUtil;
import org.eclipse.jetty.server.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatusServlet extends HttpServlet {

    private StatusInfo statusInfo;

    private ClassStatsGetter classStatsGetter;
    private ThreadStatsGetter threadStatsGetter;
    private MemoryStatsGetter memoryStatsGetter;
    private OperatingSystemStatsGetter operatingSystemStatsGetter;

    RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();

    public StatusServlet(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
        this.classStatsGetter = new ClassStatsGetter();
        this.threadStatsGetter = new ThreadStatsGetter();
        this.memoryStatsGetter = new MemoryStatsGetter();
        this.operatingSystemStatsGetter = new OperatingSystemStatsGetter();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String> props = new LinkedHashMap<String, String>();

        props.put("Server Start Time", new SimpleDateFormat("yyyy-MM-dd HH:ss").format(new Date(rb.getStartTime())));
        props.put("Server Up Time", TimeUtil.readableTimeInterval(rb.getUptime()));

        addJsonContent(props, "System ", operatingSystemStatsGetter.get().toJsonStr());
        addJsonContent(props, "Memory ", memoryStatsGetter.get().toJsonStr());
        addJsonContent(props, "Thread ", threadStatsGetter.get().toJsonStr());
        addJsonContent(props, "Class ", classStatsGetter.get().toJsonStr());
        addJsonContent(props, "GC(last Minute) ", statusInfo.getGCStats().toJsonStr());

        String jsonStr = JSON.toJSONString(props);

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type, Accept");
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter writer = resp.getWriter();
        try{
            writer.write(jsonStr);
            resp.setStatus(Response.SC_OK);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void addJsonContent(Map<String, String> props, String prefix, String jsonContent) throws IOException {
        Map<Object, Object> m = JSON.parseObject(jsonContent, Map.class);
        		//mapper.readValue(jsonContent, Map.class);
        for (Map.Entry<Object,Object> entry : m.entrySet()) {
            props.put(prefix + entry.getKey(), (String) entry.getValue());
        }
    }
}
