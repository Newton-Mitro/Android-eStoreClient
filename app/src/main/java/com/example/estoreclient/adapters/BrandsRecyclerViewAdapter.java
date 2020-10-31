package com.example.estoreclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estoreclient.R;
import com.example.estoreclient.interfaces.BrandItemClickListener;
import com.example.estoreclient.models.Brand;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BrandsRecyclerViewAdapter extends RecyclerView.Adapter<BrandsRecyclerViewAdapter.MyViewHolder>{
        private ArrayList<Brand> mData;
        private BrandItemClickListener brandItemClickListener;
        Context mContext;

        public BrandsRecyclerViewAdapter(Context mContext, ArrayList<Brand> mData, BrandItemClickListener brandItemClickListener) {
            this.mData = mData;
            this.mContext = mContext;
            this.brandItemClickListener = brandItemClickListener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.list_item_category_layout, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            Brand brand;
            brand = mData.get(i);
            myViewHolder.category_name.setText(brand.getBrand_name());
            Picasso.get()
                    .load(brand.getBrand_image())
                    .resize(50, 50)
                    .placeholder(R.drawable.simple_product)
                    .error(R.drawable.simple_product)
                    .centerCrop()
                    .into(myViewHolder.category_image);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView category_name;
            ImageView category_image;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                //Set on click listener to list items click event
                itemView.setOnClickListener(this);

                category_name = itemView.findViewById(R.id.category_list_item_text_id);
                category_image = itemView.findViewById(R.id.category_list_item_image_id);
            }

            @Override
            public void onClick(View v) {
                //Get the position of the row clicked
                int position = getAdapterPosition();
                Brand brand =  mData.get(position);
               brandItemClickListener.onBrandItemClick(brand.getId());

            }
        }

}
