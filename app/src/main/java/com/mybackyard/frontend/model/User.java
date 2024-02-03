package com.mybackyard.frontend.model;

import java.util.List;

public class User {
    private long id;
    private String first;
    private String last;
    private String email;
    private String apiKey;
    private List<Long> yardIds;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Long> getYardIds() {
        return yardIds;
    }

    public void setYardIds(List<Long> yardIds) {
        this.yardIds = yardIds;
    }
}
