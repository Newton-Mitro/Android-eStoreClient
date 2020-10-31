package com.example.estoreclient.models;

import java.io.Serializable;

public class ServiceZone implements Serializable {
    private int id;
    private String zone_name;

    public ServiceZone() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }
}
