package com.example.estoreclient.utils;

import com.example.estoreclient.models.Brand;
import com.example.estoreclient.models.Category;
import com.example.estoreclient.models.Product;
import com.example.estoreclient.models.ServiceZone;
import com.example.estoreclient.models.SubCategory;

import java.util.ArrayList;

public class GlobalData {
    public static ArrayList<Product> cartItems = new ArrayList<>();
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Brand> brands = new ArrayList<>();
    public static ArrayList<Category> categories = new ArrayList<>();
    public static ArrayList<SubCategory> subCategories = new ArrayList<>();
    public static ArrayList<ServiceZone> serviceZones = new ArrayList<>();
    public static double shipping_cost = 20.00;
    public static double grand_total = 0.00;

    public static boolean product_flug;
    public static boolean offered_flug;
    public static boolean special_product_flug;
    public static boolean featured_flug;
    public static boolean product_by_brand_flug;
    public static boolean product_by_subcategory_flug;

    public static int selected_brand;
    public static int selected_category;
}
