package com.ir.smartcity.job;

import java.util.List;

public class Result {

    private List<Route> routes;
    private String status;

    public List getRoutes() {
        return routes;
    }
    public void setRoutes(List<Route> routes)
    {
        this.routes=routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
