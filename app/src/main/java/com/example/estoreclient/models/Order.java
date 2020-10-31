package com.example.estoreclient.models;

public class Order {
    private int id;
    private int service_zone_id;
    private int user_id;
    private int status_id;
    private int shipping_detail_id;
    private String created_at;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getService_zone_id() {
        return service_zone_id;
    }

    public void setService_zone_id(int service_zone_id) {
        this.service_zone_id = service_zone_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getShipping_detail_id() {
        return shipping_detail_id;
    }

    public void setShipping_detail_id(int shipping_detail_id) {
        this.shipping_detail_id = shipping_detail_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
