package com.example.estoreclient.models;

public class SubCategory {
    private int id;
    private int category_id;
    private String subcategory_name;
    private String subcategory_image;

    public SubCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getSubcategory_image() {
        return subcategory_image;
    }

    public void setSubcategory_image(String subcategory_image) {
        this.subcategory_image = subcategory_image;
    }
}
