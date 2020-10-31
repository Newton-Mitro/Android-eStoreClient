package com.example.estoreclient.models;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String brand_name;
    private String subcategory_name;
    private String category_name;
    private String product_name;
    private String product_code;
    private double tag_price;
    private double sale_price;
    private String product_image;
    private int has_offer;
    private int featured;
    private int product_status;
    private int quantity;
    private double amount;
    private String product_short_description;
    private String product_long_description;
    private String product_height;
    private String product_width;
    private String product_weight;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public double getTag_price() {
        return tag_price;
    }

    public void setTag_price(double tag_price) {
        this.tag_price = tag_price;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getHas_offer() {
        return has_offer;
    }

    public void setHas_offer(int has_offer) {
        this.has_offer = has_offer;
    }

    public int getFeatured() {
        return featured;
    }

    public void setFeatured(int featured) {
        this.featured = featured;
    }

    public int getProduct_status() {
        return product_status;
    }

    public void setProduct_status(int product_status) {
        this.product_status = product_status;
    }

    public String getProduct_short_description() {
        return product_short_description;
    }

    public void setProduct_short_description(String product_short_description) {
        this.product_short_description = product_short_description;
    }

    public String getProduct_long_description() {
        return product_long_description;
    }

    public void setProduct_long_description(String product_long_description) {
        this.product_long_description = product_long_description;
    }

    public String getProduct_height() {
        return product_height;
    }

    public void setProduct_height(String product_height) {
        this.product_height = product_height;
    }

    public String getProduct_width() {
        return product_width;
    }

    public void setProduct_width(String product_width) {
        this.product_width = product_width;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }
}
