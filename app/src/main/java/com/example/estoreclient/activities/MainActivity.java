package com.example.estoreclient.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.estoreclient.R;
import com.example.estoreclient.adapters.BrandsRecyclerViewAdapter;
import com.example.estoreclient.adapters.CategoriesRecyclerViewAdapter;
import com.example.estoreclient.adapters.ProductsRecyclerViewAdapter;
import com.example.estoreclient.adapters.SpecailProductsRecyclerViewAdapter;
import com.example.estoreclient.adapters.SubCategoriesRecyclerViewAdapter;
import com.example.estoreclient.interfaces.BrandItemClickListener;
import com.example.estoreclient.interfaces.CategoryItemClickListener;
import com.example.estoreclient.interfaces.ProductButtonClickListener;
import com.example.estoreclient.interfaces.SubCategoryItemClickListener;
import com.example.estoreclient.models.Brand;
import com.example.estoreclient.models.Category;
import com.example.estoreclient.models.Product;
import com.example.estoreclient.models.ServiceZone;
import com.example.estoreclient.models.SubCategory;
import com.example.estoreclient.newtorks.NetworkConnectionCheck;
import com.example.estoreclient.utils.Cart;
import com.example.estoreclient.utils.Constants;
import com.example.estoreclient.utils.GlobalData;
import com.example.estoreclient.utils.dialogboxes.ExitDialog;
import com.example.estoreclient.utils.dialogboxes.NetworkConnectionDialog;
import com.example.estoreclient.utils.dialogboxes.ServiceZoneSelectionDialog;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BrandItemClickListener, CategoryItemClickListener, ProductButtonClickListener, SubCategoryItemClickListener {
    private LinearLayout mStore;
    private TextView mStoreText;
    private LinearLayout mCategory;
    private TextView mCategoryText;
    private LinearLayout mBrand;
    private TextView mBrandText;
    private LinearLayout mOffer;
    private TextView mOfferText;
    private LinearLayout mFeatured;
    private CardView mEmptyView;
    private TextView mFeaturedText;
    private TextView cart_total;
    private LinearLayout mBestSeller;
    private LinearLayout cartTotal_container;
    private TextView mBestSellerText;
    private RecyclerView recyclerView;
    private Cart cart;
    private CardView bottom_cart_bar;
    private ViewGroup.MarginLayoutParams layoutParams;
    private ProductsRecyclerViewAdapter productsRecyclerViewAdapter;
    private ServiceZoneSelectionDialog serviceZoneSelectionDialog;
    private CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter;
    private SubCategoriesRecyclerViewAdapter subCategoriesRecyclerViewAdapter;
    private BrandsRecyclerViewAdapter brandsRecyclerViewAdapter;
    private SpecailProductsRecyclerViewAdapter specailProductsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_eco_24);


        mStore = findViewById(R.id.home_menu_container_id);
        mStoreText = findViewById(R.id.home_menu_text_id);
        mCategory = findViewById(R.id.categories_menu_container_id);
        mCategoryText = findViewById(R.id.categories_menu_text_id);
        mBrand = findViewById(R.id.brands_menu_container_id);
        mBrandText = findViewById(R.id.brands_menu_text_id);
        mOffer = findViewById(R.id.offered_menu_container_id);
        mOfferText = findViewById(R.id.offered_menu_text_id);
        mFeatured = findViewById(R.id.featured_menu_container_id);
        mFeaturedText = findViewById(R.id.featured_menu_text_id);
        mBestSeller = findViewById(R.id.best_selling_menu_container_id);
        mBestSellerText = findViewById(R.id.best_selling_menu_text_id);
        bottom_cart_bar = findViewById(R.id.bottom_cart_bar_id);
        recyclerView = findViewById(R.id.home_recycler_view_id);
        cart_total = findViewById(R.id.cart_total_amount_text_id);
        cartTotal_container = findViewById(R.id.cart_total_container_id);

        cart = new Cart();
        if (cart.getCartTotal() == 0) {
            bottom_cart_bar.setVisibility(View.GONE);
            setContentTopMargin();
        } else {
            bottom_cart_bar.setVisibility(View.VISIBLE);
            cart_total.setText(Double.toString(cart.getCartTotal()));
            setContentBottomMargin();
        }

        mEmptyView = findViewById(R.id.empty_view_id);
        recyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);

        mStore.setOnClickListener(this);
        mCategory.setOnClickListener(this);
        mBrand.setOnClickListener(this);
        mOffer.setOnClickListener(this);
        mFeatured.setOnClickListener(this);
        mBestSeller.setOnClickListener(this);
        cartTotal_container.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if (b != null){
            if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                if (!b.getBoolean("visible_at_startup")){
                    getServiceZones();
                }
            }
        }

        if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
            getProducts();
        } else {
            NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
            networkConnectionDialog.showDialog();
        }

    }

    public void setContentTopMargin() {
        int actionBarHeight;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            layoutParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
            layoutParams.setMargins(0, actionBarHeight - 5, 0, 0);
            recyclerView.requestLayout();
        }
    }

    public void setContentBottomMargin() {
        int actionBarHeight;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            layoutParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
            layoutParams.setMargins(0, actionBarHeight - 5, 0, actionBarHeight - 5);
            recyclerView.requestLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getProductsBySearchText(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                finishAffinity();
                return true;
            case R.id.action_exit:
                finishAffinity();
                System.exit(0);
                return true;
            case R.id.action_service_zone:
                if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                    getServiceZones();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                    networkConnectionDialog.showDialog();
                }
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_menu_container_id:
                if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                    getProducts();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                    networkConnectionDialog.showDialog();
                }
                GlobalData.product_flug = true;
                GlobalData.offered_flug = false;
                GlobalData.featured_flug = false;
                GlobalData.special_product_flug = false;
                GlobalData.product_by_brand_flug = false;
                GlobalData.product_by_subcategory_flug = false;
                break;
            case R.id.categories_menu_container_id:
                if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                    getCategories();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                    networkConnectionDialog.showDialog();
                }
                break;
            case R.id.brands_menu_container_id:
                if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                    getBrands();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                    networkConnectionDialog.showDialog();
                }
                break;
            case R.id.offered_menu_container_id:
                if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                    getOfferedProducts();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                    networkConnectionDialog.showDialog();
                }
                GlobalData.product_flug = false;
                GlobalData.offered_flug = true;
                GlobalData.featured_flug = false;
                GlobalData.special_product_flug = false;
                GlobalData.product_by_brand_flug = false;
                GlobalData.product_by_subcategory_flug = false;
                break;
            case R.id.featured_menu_container_id:
                if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                    getCategories();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                    networkConnectionDialog.showDialog();
                }
                GlobalData.product_flug = false;
                GlobalData.offered_flug = false;
                GlobalData.featured_flug = true;
                GlobalData.special_product_flug = false;
                GlobalData.product_by_brand_flug = false;
                GlobalData.product_by_subcategory_flug = false;

                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(Color.parseColor("#353535"));
                mBrandText.setTextColor(Color.parseColor("#353535"));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(getResources().getColor(R.color.colorAccent));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
                break;
            case R.id.best_selling_menu_container_id:
                if (NetworkConnectionCheck.isNetworkAvailable(MainActivity.this)) {
                    getCategories();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                    networkConnectionDialog.showDialog();
                }
                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(Color.parseColor("#353535"));
                mBrandText.setTextColor(Color.parseColor("#353535"));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case R.id.cart_total_container_id:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                finishAffinity();
                break;

        }
    }

    private void getServiceZones() {
        GlobalData.product_flug = true;
        GlobalData.offered_flug = false;
        GlobalData.featured_flug = false;
        GlobalData.special_product_flug = false;
        GlobalData.product_by_brand_flug = false;
        GlobalData.product_by_subcategory_flug = false;

        GlobalData.serviceZones.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queueZone = Volley.newRequestQueue(this);
        JsonArrayRequest jsonServiceZoneArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.get_service_zone_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                ServiceZone serviceZone = gson.fromJson(String.valueOf(jsonCategoryObject), ServiceZone.class);
                                GlobalData.serviceZones.add(serviceZone);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queueZone.add(jsonServiceZoneArrayRequest);

        queueZone.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                serviceZoneSelectionDialog = new ServiceZoneSelectionDialog(MainActivity.this);
                serviceZoneSelectionDialog.showDialog();
            }
        });
    }

    private void getProducts() {
        GlobalData.products.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonProductsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.get_all_product_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Product product = gson.fromJson(String.valueOf(jsonCategoryObject), Product.class);
                                GlobalData.products.add(product);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonProductsArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                int index = 0;
                for (Product product : GlobalData.products) {
                    if (GlobalData.cartItems != null) {
                        for (Product item : GlobalData.cartItems) {
                            if (item.getId() == product.getId()) {
                                GlobalData.products.set(index, item);
                            }
                        }
                    }
                    index++;
                }
                recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.products.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(MainActivity.this, GlobalData.products, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(productsRecyclerViewAdapter);
                productsRecyclerViewAdapter.notifyDataSetChanged();
                mStoreText.setTextColor(getResources().getColor(R.color.colorAccent));
                mCategoryText.setTextColor(Color.parseColor("#353535"));
                mBrandText.setTextColor(Color.parseColor("#353535"));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
            }
        });
    }

    private void getProductsBySearchText(String searchText) {
        GlobalData.products.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonProductsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.get_by_search_text_product_url+searchText,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Product product = gson.fromJson(String.valueOf(jsonCategoryObject), Product.class);
                                GlobalData.products.add(product);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonProductsArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                int index = 0;
                for (Product product : GlobalData.products) {
                    if (GlobalData.cartItems != null) {
                        for (Product item : GlobalData.cartItems) {
                            if (item.getId() == product.getId()) {
                                GlobalData.products.set(index, item);
                            }
                        }
                    }
                    index++;
                }
                recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.products.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(MainActivity.this, GlobalData.products, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(productsRecyclerViewAdapter);
                productsRecyclerViewAdapter.notifyDataSetChanged();

            }
        });
    }

    private void getCategories() {
        GlobalData.categories.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonCategoryArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.get_category_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Category category = gson.fromJson(String.valueOf(jsonCategoryObject), Category.class);
                                GlobalData.categories.add(category);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonCategoryArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.categories.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(MainActivity.this, GlobalData.categories, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(categoriesRecyclerViewAdapter);
                categoriesRecyclerViewAdapter.notifyDataSetChanged();
                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(getResources().getColor(R.color.colorAccent));
                mBrandText.setTextColor(Color.parseColor("#353535"));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
            }
        });
    }

    private void getSubcategoriesByCategoryId(int id) {
        GlobalData.subCategories.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonCategoryArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.get_subcategory_by_category_id_url+id,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonSubCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                SubCategory subCategory = gson.fromJson(String.valueOf(jsonSubCategoryObject), SubCategory.class);
                                GlobalData.subCategories.add(subCategory);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonCategoryArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.subCategories.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }

                subCategoriesRecyclerViewAdapter = new SubCategoriesRecyclerViewAdapter(MainActivity.this, GlobalData.subCategories, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(subCategoriesRecyclerViewAdapter);
                subCategoriesRecyclerViewAdapter.notifyDataSetChanged();
                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(getResources().getColor(R.color.colorAccent));
                mBrandText.setTextColor(Color.parseColor("#353535"));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
            }
        });
    }

    private void getBrands() {
        GlobalData.brands.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonBrandsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.get_brand_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonBrandObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Brand brand = gson.fromJson(String.valueOf(jsonBrandObject), Brand.class);
                                GlobalData.brands.add(brand);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonBrandsArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.brands.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                brandsRecyclerViewAdapter = new BrandsRecyclerViewAdapter(MainActivity.this, GlobalData.brands, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(brandsRecyclerViewAdapter);
                brandsRecyclerViewAdapter.notifyDataSetChanged();
                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(Color.parseColor("#353535"));
                mBrandText.setTextColor(getResources().getColor(R.color.colorAccent));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
            }
        });
    }

    private void getOfferedProducts() {
        GlobalData.products.clear();
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonProductsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Constants.get_offered_product_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Product product = gson.fromJson(String.valueOf(jsonCategoryObject), Product.class);
                                GlobalData.products.add(product);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonProductsArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                int index = 0;
                for (Product product : GlobalData.products) {
                    if (GlobalData.cartItems != null) {
                        for (Product item : GlobalData.cartItems) {
                            if (item.getId() == product.getId()) {
                                GlobalData.products.set(index, item);
                            }
                        }
                    }
                    index++;
                }
                recyclerView = findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.products.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                specailProductsRecyclerViewAdapter = new SpecailProductsRecyclerViewAdapter(MainActivity.this, GlobalData.products, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(specailProductsRecyclerViewAdapter);
                specailProductsRecyclerViewAdapter.notifyDataSetChanged();
                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(Color.parseColor("#353535"));
                mBrandText.setTextColor(Color.parseColor("#353535"));
                mOfferText.setTextColor(getResources().getColor(R.color.colorAccent));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
            }
        });
    }

    private void getProductsByBrandId(int position) {
        GlobalData.products.clear();
        VolleyLog.DEBUG = true;
        String url = Constants.get_by_brand_product_url + position;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonProductsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Product product = gson.fromJson(String.valueOf(jsonCategoryObject), Product.class);
                                GlobalData.products.add(product);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonProductsArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                int index = 0;
                for (Product product : GlobalData.products) {
                    if (GlobalData.cartItems != null) {
                        for (Product item : GlobalData.cartItems) {
                            if (item.getId() == product.getId()) {
                                GlobalData.products.set(index, item);
                            }
                        }
                    }
                    index++;
                }
                recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.products.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(MainActivity.this, GlobalData.products, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(productsRecyclerViewAdapter);
                productsRecyclerViewAdapter.notifyDataSetChanged();
                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(Color.parseColor("#353535"));
                mBrandText.setTextColor(getResources().getColor(R.color.colorAccent));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
            }
        });
    }

    private void getProductsBySubcategoryId(int position) {
        GlobalData.products.clear();
        VolleyLog.DEBUG = true;
        String url = Constants.get_by_subcategory_product_url + position;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonProductsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonCategoryObject = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Product product = gson.fromJson(String.valueOf(jsonCategoryObject), Product.class);
                                GlobalData.products.add(product);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, errorListener) {
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        queue.add(jsonProductsArrayRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onRequestFinished(Request<Object> request) {
                if (request.getCacheEntry() != null) {
                    String cacheValue = null;
                    try {
                        cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                int index = 0;
                for (Product product : GlobalData.products) {
                    if (GlobalData.cartItems != null) {
                        for (Product item : GlobalData.cartItems) {
                            if (item.getId() == product.getId()) {
                                GlobalData.products.set(index, item);
                            }
                        }
                    }
                    index++;
                }
                recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view_id);
                mEmptyView = findViewById(R.id.empty_view_id);
                if (GlobalData.products.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                productsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(MainActivity.this, GlobalData.products, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(productsRecyclerViewAdapter);
                productsRecyclerViewAdapter.notifyDataSetChanged();
                mStoreText.setTextColor(Color.parseColor("#353535"));
                mCategoryText.setTextColor(getResources().getColor(R.color.colorAccent));
                mBrandText.setTextColor(Color.parseColor("#353535"));
                mOfferText.setTextColor(Color.parseColor("#353535"));
                mFeaturedText.setTextColor(Color.parseColor("#353535"));
                mBestSellerText.setTextColor(Color.parseColor("#353535"));
            }
        });
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(MainActivity.this);
                networkConnectionDialog.showDialog();
            } else {
                Log.d("TAG", "onErrorResponse: "+error.toString());
            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        cart = new Cart();
        if (cart.getCartTotal() == 0) {
            bottom_cart_bar.setVisibility(View.GONE);
            setContentTopMargin();
        } else {
            bottom_cart_bar.setVisibility(View.VISIBLE);
            setContentBottomMargin();
            cart_total = findViewById(R.id.cart_total_amount_text_id);
            cart_total.setText(Double.toString(cart.getCartTotal()));
        }
    }

    @Override
    public void onBrandItemClick(int position) {
        getProductsByBrandId(position);
        GlobalData.product_flug = false;
        GlobalData.offered_flug = false;
        GlobalData.featured_flug = false;
        GlobalData.special_product_flug = false;
        GlobalData.product_by_brand_flug = true;
        GlobalData.product_by_subcategory_flug = false;
    }

    @Override
    public void onCategoryItemClick(int id) {
        getSubcategoriesByCategoryId(id);
        GlobalData.product_flug = false;
        GlobalData.offered_flug = false;
        GlobalData.featured_flug = false;
        GlobalData.special_product_flug = false;
        GlobalData.product_by_brand_flug = false;
        GlobalData.product_by_subcategory_flug = true;
    }

    @Override
    public void onSubCategoryItemClick(int id) {
        getProductsBySubcategoryId(id);
        GlobalData.product_flug = false;
        GlobalData.offered_flug = false;
        GlobalData.featured_flug = false;
        GlobalData.special_product_flug = false;
        GlobalData.product_by_brand_flug = false;
        GlobalData.product_by_subcategory_flug = true;
    }

    @Override
    public void onAddToCartButtonClick(Product product) {
        cart = new Cart();
        if (cart.getCartTotal() == 0) {
            bottom_cart_bar.setVisibility(View.GONE);
            setContentTopMargin();
        } else {
            bottom_cart_bar.setVisibility(View.VISIBLE);
            setContentBottomMargin();
            cart_total = findViewById(R.id.cart_total_amount_text_id);
            cart_total.setText(Double.toString(cart.getCartTotal()));
        }
    }

    @Override
    public void onIncreaseButtonClick(Product product) {
        cart = new Cart();
        if (cart.getCartTotal() == 0) {
            bottom_cart_bar.setVisibility(View.GONE);
            setContentTopMargin();
        } else {
            bottom_cart_bar.setVisibility(View.VISIBLE);
            setContentBottomMargin();
            cart_total = findViewById(R.id.cart_total_amount_text_id);
            cart_total.setText(Double.toString(cart.getCartTotal()));
        }
    }

    @Override
    public void onDecreaseButtonClick(Product product) {
        cart = new Cart();
        if (cart.getCartTotal() == 0) {
            bottom_cart_bar.setVisibility(View.GONE);
            setContentTopMargin();
        } else {
            bottom_cart_bar.setVisibility(View.VISIBLE);
            setContentBottomMargin();
            cart_total = findViewById(R.id.cart_total_amount_text_id);
            cart_total.setText(Double.toString(cart.getCartTotal()));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog(this);
        exitDialog.showDialog();
    }
}