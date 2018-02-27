package com.appdy.metricloader.dto;

public class JsonEntityMap {
    private long time;
    private String controller;
    private Application[] applications;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public Application[] getApplications() {
        return applications;
    }

    public void setApplications(Application[] applications) {
        this.applications = applications;
    }
}
