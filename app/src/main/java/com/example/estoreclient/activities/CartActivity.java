package com.example.estoreclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.estoreclient.R;
import com.example.estoreclient.adapters.CartProductsRecyclerViewAdapter;
import com.example.estoreclient.interfaces.ProductButtonClickListener;
import com.example.estoreclient.models.Product;
import com.example.estoreclient.models.ServiceZone;
import com.example.estoreclient.newtorks.NetworkConnectionCheck;
import com.example.estoreclient.utils.Cart;
import com.example.estoreclient.utils.Constants;
import com.example.estoreclient.utils.GlobalData;
import com.example.estoreclient.utils.dialogboxes.ExitDialog;
import com.example.estoreclient.utils.dialogboxes.NetworkConnectionDialog;
import com.example.estoreclient.utils.dialogboxes.ServiceZoneSelectionDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CartActivity extends AppCompatActivity implements ProductButtonClickListener {
    private RecyclerView cartRecyclerView;
    private CartProductsRecyclerViewAdapter cartProductsRecyclerViewAdapter;
    private CardView bottom_summery;
    private CardView empty_cart;
    private Button place_order_button;
    private Button shop_button;
    private ImageButton home_button;
    private ImageButton exit_button;
    private TextView subtotal;
    private TextView shipping;
    private TextView grand_total;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setIcon(R.drawable.ic_baseline_add_shopping_cart_24);

        cartRecyclerView = findViewById(R.id.cart_recycler_view_id);
        bottom_summery = findViewById(R.id.cart_bottom_summery_id);
        empty_cart = findViewById(R.id.empty_cart_view_id);
        place_order_button = findViewById(R.id.place_order_button_id);
        shop_button = findViewById(R.id.shop_button_id);
        home_button = findViewById(R.id.cart_home_button_id);
        exit_button = findViewById(R.id.cart_exit_button_id);
        subtotal = findViewById(R.id.subtotal_amount_id);
        shipping = findViewById(R.id.shipping_cost_amount_id);
        grand_total = findViewById(R.id.grand_total_amount_id);

        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this,ShippingDetailsActivity.class);
                startActivity(intent);
            }
        });

        shop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Intent intent = new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Intent intent = new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });

        if (GlobalData.cartItems.isEmpty()) {
            bottom_summery.setVisibility(View.GONE);
            empty_cart.setVisibility(View.VISIBLE);
        } else {
            bottom_summery.setVisibility(View.VISIBLE);
            empty_cart.setVisibility(View.GONE);
            cartProductsRecyclerViewAdapter = new CartProductsRecyclerViewAdapter(CartActivity.this, GlobalData.cartItems, CartActivity.this);
            cartRecyclerView.setHasFixedSize(true);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
            cartRecyclerView.setAdapter(cartProductsRecyclerViewAdapter);
            setCartDetails();
        }

    }

    private void setCartDetails() {
        cart = new Cart();
        subtotal.setText(Double.toString(cart.getCartTotal()));
        shipping.setText(Double.toString(GlobalData.shipping_cost));
        grand_total.setText(Double.toString(GlobalData.shipping_cost + cart.getCartTotal()));
    }

    private void getServiceZones() {
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

                ServiceZoneSelectionDialog serviceZoneSelectionDialog = new ServiceZoneSelectionDialog(CartActivity.this);
                serviceZoneSelectionDialog.showDialog();
            }
        });
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(CartActivity.this);
            } else {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
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
            case R.id.action_home:
                finishAffinity();
                Intent intent = new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                finishAffinity();
                System.exit(0);
                return true;
            case R.id.action_service_zone:
                if (NetworkConnectionCheck.isNetworkAvailable(CartActivity.this)){
                    getServiceZones();
                }else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(CartActivity.this);
                    networkConnectionDialog.showDialog();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setCartDetails();
    }

    @Override
    public void onAddToCartButtonClick(Product product) {
        cartProductsRecyclerViewAdapter.notifyDataSetChanged();
        setCartDetails();
    }

    @Override
    public void onIncreaseButtonClick(Product product) {
        cartProductsRecyclerViewAdapter.notifyDataSetChanged();
        setCartDetails();
    }

    @Override
    public void onDecreaseButtonClick(Product product) {
        cartProductsRecyclerViewAdapter.notifyDataSetChanged();
        if (GlobalData.cartItems.isEmpty()) {
            bottom_summery.setVisibility(View.GONE);
            empty_cart.setVisibility(View.VISIBLE);
        } else {
            bottom_summery.setVisibility(View.VISIBLE);
            empty_cart.setVisibility(View.GONE);
            setCartDetails();
        }
    }

    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog(this);
        exitDialog.showDialog();
    }


}