package com.example.estoreclient.utils.wrappers;


import com.example.estoreclient.models.Brand;

import java.io.Serializable;
import java.util.ArrayList;

public class BrandDataWrapper  implements Serializable {
    private ArrayList<Brand> brands;

    public BrandDataWrapper(ArrayList<Brand> data) {
        this.brands = data;
    }

    public ArrayList<Brand> getBrands() {
        return this.brands;
    }
}
