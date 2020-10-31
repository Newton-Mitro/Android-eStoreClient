package com.example.estoreclient.utils.wrappers;


import com.example.estoreclient.models.Category;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryDataWrapper  implements Serializable {
    private ArrayList<Category> categories;

    public CategoryDataWrapper(ArrayList<Category> data) {
        this.categories = data;
    }

    public ArrayList<Category> getCategories() {
        return this.categories;
    }
}
