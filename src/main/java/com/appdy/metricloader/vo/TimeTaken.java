package com.appdy.metricloader.vo;

import java.util.Collection;
import java.util.Map;

public class TimeTaken {
    private Map<String, Collection<ApiTime>> apiTimeMap;
    private long totalTime;

    public TimeTaken() {
    }

    public TimeTaken(Map<String, Collection<ApiTime>> apiTimeMap, long totalTime) {
        this.apiTimeMap = apiTimeMap;
        this.totalTime = totalTime;
    }

    public Map<String, Collection<ApiTime>> getApiTimeMap() {
        return apiTimeMap;
    }

    public void setApiTimeMap(Map<String, Collection<ApiTime>> apiTimeMap) {
        this.apiTimeMap = apiTimeMap;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
