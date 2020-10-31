package com.example.estoreclient.utils.wrappers;

import com.example.estoreclient.models.ServiceZone;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceZoneDataWrapper  implements Serializable {
    private ArrayList<ServiceZone> serviceZones;

    public ServiceZoneDataWrapper(ArrayList<ServiceZone> data) {
        this.serviceZones = data;
    }

    public ArrayList<ServiceZone> getServiceZones() {
        return this.serviceZones;
    }
}
