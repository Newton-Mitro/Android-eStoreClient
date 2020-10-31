package com.example.estoreclient.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estoreclient.R;
import com.example.estoreclient.db.TinyDB;
import com.example.estoreclient.models.ServiceZone;

import java.util.ArrayList;


public class ServiceZoneRecyclerViewAdapter extends RecyclerView.Adapter<ServiceZoneRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<ServiceZone> mData;
    Context mContext;
    private Dialog dialog;

    public ServiceZoneRecyclerViewAdapter(Context mContext, ArrayList<ServiceZone> mData, Dialog dialog) {
        this.mData = mData;
        this.mContext = mContext;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_item_service_zone_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ServiceZone serviceZone;
        serviceZone = mData.get(i);
//        myViewHolder.service_zone_name.setTextColor(255);
        myViewHolder.service_zone_name.setText(serviceZone.getZone_name());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView service_zone_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Set on click listener to list items click event
            itemView.setOnClickListener(this);

            service_zone_name = itemView.findViewById(R.id.service_zone_text_id);
        }

        @Override
        public void onClick(View v) {
            //Get the position of the row clicked
            int position = getAdapterPosition();
            ServiceZone serviceZone = mData.get(position);
//            Toast.makeText(mContext, serviceZone.getZone_name(), Toast.LENGTH_SHORT).show();
            TinyDB tinydb = new TinyDB(mContext);
            tinydb.putInt("service_zone_id", serviceZone.getId());
            dialog.cancel();

        }
    }

}
