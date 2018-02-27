package com.appdy.metricloader.vo;

public class ApiTime {
    private String api;
    private long timeTaken;
    private String exception;

    public ApiTime() {
    }

    public ApiTime(String api, long timeTaken) {
        this.api = api;
        this.timeTaken = timeTaken;
    }

    public ApiTime(String api, long timeTaken, String exception) {
        this.api = api;
        this.timeTaken = timeTaken;
        this.exception = exception;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

}
