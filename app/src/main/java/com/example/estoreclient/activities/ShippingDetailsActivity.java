package com.example.estoreclient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.estoreclient.R;
import com.example.estoreclient.db.TinyDB;
import com.example.estoreclient.models.ServiceZone;
import com.example.estoreclient.newtorks.NetworkConnectionCheck;
import com.example.estoreclient.utils.Constants;
import com.example.estoreclient.utils.GlobalData;
import com.example.estoreclient.utils.dialogboxes.NetworkConnectionDialog;
import com.example.estoreclient.utils.dialogboxes.OrderInfoDialog;
import com.example.estoreclient.utils.dialogboxes.OrderSuccessDialog;
import com.example.estoreclient.utils.dialogboxes.ServiceZoneSelectionDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ShippingDetailsActivity extends AppCompatActivity {
    private Button submit;
    private Button back_to_cart;
    private EditText mFirst_name;
    private EditText mLast_name;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mAddress;

    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String address;
    private int service_zone_id;
    TinyDB tinyDB;
    private String cart_items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Shipping & Billing");
        getSupportActionBar().setIcon(R.drawable.ic_baseline_local_shipping_24);
        tinyDB = new TinyDB(this);

        mFirst_name = findViewById(R.id.first_name_text_id);
        mLast_name = findViewById(R.id.last_name_text_id);
        mEmail = findViewById(R.id.email_text_id);
        mPhone = findViewById(R.id.phone_text_id);
        mAddress = findViewById(R.id.address_text_id);

        mFirst_name.setText(tinyDB.getString("first_name"));
        mLast_name.setText(tinyDB.getString("last_name"));
        mEmail.setText(tinyDB.getString("email"));
        mPhone.setText(tinyDB.getString("phone"));
        mAddress.setText(tinyDB.getString("address"));


        submit = findViewById(R.id.submit_button_id);
        back_to_cart = findViewById(R.id.back_to_cart_button_id);

        back_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tinyDB.getInt("service_zone_id")>0){
                    if (NetworkConnectionCheck.isNetworkAvailable(ShippingDetailsActivity.this)){
                        submit();
                    }else {
                        NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(ShippingDetailsActivity.this);
                        networkConnectionDialog.showDialog();
                    }
                }else {
                    if (NetworkConnectionCheck.isNetworkAvailable(ShippingDetailsActivity.this)){
                        getServiceZones();
                    }else {
                        NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(ShippingDetailsActivity.this);
                        networkConnectionDialog.showDialog();
                    }
                }

            }
        });
    }

    public void submit() {
        validateInput();
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Placing order.. Please, wait....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.post_place_order_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.cancel();
                        try {
                            if (response.trim().matches("success")) {
                                GlobalData.cartItems.clear();
                                OrderSuccessDialog orderSuccessDialog = new OrderSuccessDialog(ShippingDetailsActivity.this);
                                orderSuccessDialog.showDialog();
                            } else {
                                OrderInfoDialog orderInfoDialog = new OrderInfoDialog(ShippingDetailsActivity.this);
                                orderInfoDialog.showDialog();
                            }
                        } catch (Exception ex) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.cancel();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("shipping_f_name", first_name);
                params.put("shipping_l_name", last_name);
                params.put("shipping_address", address);
                params.put("shipping_phone", phone);
                params.put("shipping_email", email);
                params.put("service_zone_id", Integer.toString(service_zone_id));
                params.put("user_id", Integer.toString(3));
                params.put("order_items", cart_items);
                return params;
            }
        };

        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });

    }

    public void validateInput() {
        first_name = mFirst_name.getText().toString();
        last_name = mLast_name.getText().toString();
        email = mEmail.getText().toString();
        phone = mPhone.getText().toString();
        address = mAddress.getText().toString();


        service_zone_id = tinyDB.getInt("service_zone_id");

        tinyDB.putString("first_name", first_name);
        tinyDB.putString("last_name", last_name);
        tinyDB.putString("email", email);
        tinyDB.putString("phone", phone);
        tinyDB.putString("address", address);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        cart_items = gson.toJson(GlobalData.cartItems);

        if (mFirst_name.getText().toString().trim().isEmpty()) {
            mFirst_name.setError("Please input your first name");
            mFirst_name.requestFocus();
            return;
        } else if (mLast_name.getText().toString().trim().isEmpty()) {
            mLast_name.setError("this field must not be empty");
            mLast_name.requestFocus();
            return;
        } else if (mPhone.length() != 11 & TextUtils.isDigitsOnly(mPhone.getText())) {
            mPhone.setError("Please give a valid phone number");
            mPhone.requestFocus();
            return;
        } else if (mPhone.getText().toString().trim().isEmpty()) {
            mPhone.setError("Please give your phone number");
            mPhone.requestFocus();
            return;
        } else if (mAddress.getText().toString().trim().isEmpty()) {
            mAddress.setError("this field must not be empty");
            mAddress.requestFocus();
            return;
        }

        tinyDB.putString("first_name", first_name);
        tinyDB.putString("last_name", last_name);
        tinyDB.putString("email", email);
        tinyDB.putString("phone", phone);
        tinyDB.putString("address", address);
    }

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
                Intent intent = new Intent(ShippingDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_exit:
                finishAffinity();
                System.exit(0);
                return true;
            case R.id.action_service_zone:
                if (NetworkConnectionCheck.isNetworkAvailable(ShippingDetailsActivity.this)) {
                    getServiceZones();
                } else {
                    NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(ShippingDetailsActivity.this);
                    networkConnectionDialog.showDialog();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
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

                ServiceZoneSelectionDialog serviceZoneSelectionDialog = new ServiceZoneSelectionDialog(ShippingDetailsActivity.this);
                serviceZoneSelectionDialog.showDialog();
            }
        });
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                NetworkConnectionDialog networkConnectionDialog = new NetworkConnectionDialog(ShippingDetailsActivity.this);
            } else {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };
}