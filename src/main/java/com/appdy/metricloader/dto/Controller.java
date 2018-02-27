package com.appdy.metricloader.dto;

import java.util.Objects;

public class Controller {
    protected String url;
    protected String user;
    protected String account;
    protected String password;

    public Controller() {
    }

    public Controller(String url, String account, String user, String password) {
        this.url = url;
        this.user = user;
        this.account = account;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Controller)) return false;
        Controller that = (Controller) o;
        return Objects.equals(getUrl(), that.getUrl()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getAccount(), that.getAccount());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUrl(), getUser(), getAccount());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Controller{");
        sb.append("url='").append(url).append('\'');
        sb.append(", user='").append(user).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
