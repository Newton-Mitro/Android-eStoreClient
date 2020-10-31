package com.example.estoreclient.utils.wrappers;


import com.example.estoreclient.models.Product;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductDataWrapper implements Serializable {

    private ArrayList<Product> products;

    public ProductDataWrapper(ArrayList<Product> data) {
        this.products = data;
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

}