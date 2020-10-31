package com.example.estoreclient.utils.dialogboxes;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estoreclient.R;
import com.example.estoreclient.adapters.ServiceZoneRecyclerViewAdapter;
import com.example.estoreclient.db.TinyDB;
import com.example.estoreclient.utils.GlobalData;


public class ServiceZoneSelectionDialog {
    private Context context;
    private ImageButton dialog_close;
    private CheckBox show_at_startup;
    RecyclerView zoneRecyclerView;
    private TinyDB tinydb;


    public ServiceZoneSelectionDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        final Dialog serviceZoneSelection = new Dialog(context);
        serviceZoneSelection.requestWindowFeature(Window.FEATURE_NO_TITLE);
        serviceZoneSelection.setContentView(R.layout.service_zone_selection_dialog_layout);
        serviceZoneSelection.setCancelable(false);
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        lp1.copyFrom(serviceZoneSelection.getWindow().getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.MATCH_PARENT;
        serviceZoneSelection.show();
        serviceZoneSelection.getWindow().setAttributes(lp1);

        dialog_close = serviceZoneSelection.findViewById(R.id.dialog_close_id);
        show_at_startup = serviceZoneSelection.findViewById(R.id.show_at_startup_id);
        tinydb = new TinyDB(context);
        if (!tinydb.getBoolean("visible_at_startup")){
            show_at_startup.setChecked(true);
        }else {
            show_at_startup.setChecked(false);
        }

        show_at_startup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!show_at_startup.isChecked()){
                    tinydb.putBoolean("visible_at_startup",true);
                }else {
                    tinydb.putBoolean("visible_at_startup",false);
                }
            }
        });

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceZoneSelection.cancel();
            }
        });
        // Show products to recycler view
        if (GlobalData.serviceZones!=null){
            zoneRecyclerView = serviceZoneSelection.findViewById(R.id.service_zone_recycler_id);
            ServiceZoneRecyclerViewAdapter serviceZoneRecyclerViewAdapter = new ServiceZoneRecyclerViewAdapter(context, GlobalData.serviceZones,serviceZoneSelection);
            zoneRecyclerView.setHasFixedSize(true);
            zoneRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            zoneRecyclerView.setAdapter(serviceZoneRecyclerViewAdapter);
        }

        // End of show products recycler view

    }

}
