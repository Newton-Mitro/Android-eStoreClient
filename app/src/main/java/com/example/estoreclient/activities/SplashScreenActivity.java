package com.example.estoreclient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.estoreclient.db.TinyDB;
import com.example.estoreclient.models.ServiceZone;
import com.example.estoreclient.newtorks.NetworkConnectionCheck;
import com.example.estoreclient.utils.Constants;
import com.example.estoreclient.utils.GlobalData;
import com.example.estoreclient.utils.dialogboxes.NetworkConnectionDialog;
import com.example.estoreclient.utils.dialogboxes.ServiceZoneSelectionDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SplashScreenActivity extends AppCompatActivity {
    private TinyDB tinydb;
    private ServiceZoneSelectionDialog serviceZoneSelectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        tinydb = new TinyDB(this);
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("visible_at_startup", tinydb.getBoolean("visible_at_startup"));
        intent.putExtras(b);
        startActivity(intent);
        finishAffinity();

    }


}