package com.example.estoreclient.utils;

public class Constants {
    //    API Routes
    public static final String site_url = "http://192.168.1.103/eStore/";

    public static final String get_all_product_url = site_url + "api/products";
    public static final String get_by_brand_product_url = site_url + "api/products/brand/";
    public static final String get_by_subcategory_product_url = site_url + "api/products/subcategory/";
    public static final String get_subcategory_by_category_id_url = site_url + "api/categories/subcategory/";
    public static final String get_by_search_text_product_url = site_url + "api/products/search/";
    public static final String get_offered_product_url = site_url + "api/products/offered";
    public static final String get_featured_product_url = site_url + "api/products/featured";
    public static final String get_best_selling_product_url = site_url + "api/products/best-selling";

    public static final String get_brand_url = site_url + "api/brands";

    public static final String get_category_url = site_url + "api/categories";

    public static final String post_place_order_url = site_url + "api/orders/store";

    public static final String get_service_zone_url = site_url + "api/zones";
    //    End of API Routes
}
