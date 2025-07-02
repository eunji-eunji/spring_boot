package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class MonitoringInterceptor implements HandlerInterceptor {

    ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<StopWatch>();

    public boolean preHandle(HttpServletRequest request, Object handelr) throws Exception {
        StopWatch stopWatch = new StopWatch(handelr.toString());
        stopWatch.start(handelr.toString());
        stopWatchLocal.set(stopWatch);
        log.info("URL: " + getURLPath(request));
        log.info("RequestStart: " + getCurrentTime());
        return true;
    }

    public void postHandle() {
        log.info("RequestEnd: " + getCurrentTime());
    }

    public void afterCompletion() {
        StopWatch stopWatch = stopWatchLocal.get();
        stopWatch.stop();
        log.info("RequestTotal: " + stopWatch.getTotalTimeMillis()+"ms");
        stopWatchLocal.set(null);
        log.info("\n");
    }

    private String getURLPath(HttpServletRequest request) {
        String currentPath = request.getRequestURI();
        String queryString = request.getQueryString();
        queryString = queryString == null ? "" : "?" + queryString;
        return currentPath + queryString;
    }

    private String getCurrentTime() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return formatter.format(calendar.getTime());
    }
}
