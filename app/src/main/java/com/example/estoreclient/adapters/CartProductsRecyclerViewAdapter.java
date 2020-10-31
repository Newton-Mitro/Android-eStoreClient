package com.example.estoreclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estoreclient.R;
import com.example.estoreclient.interfaces.ProductButtonClickListener;
import com.example.estoreclient.models.Product;
import com.example.estoreclient.utils.Cart;
import com.example.estoreclient.utils.GlobalData;
import com.example.estoreclient.utils.dialogboxes.ProductDetailsViewDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartProductsRecyclerViewAdapter extends RecyclerView.Adapter<CartProductsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Product> mData;
    private Context mContext;
    private ProductButtonClickListener productButtonClickListener;

    public CartProductsRecyclerViewAdapter(Context mContext, ArrayList<Product> mData, ProductButtonClickListener productButtonClickListener) {
        this.mData = mData;
        this.mContext = mContext;
        this.productButtonClickListener = productButtonClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_item_product_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Product product;
        product = mData.get(i);
        myViewHolder.product_name.setText(product.getProduct_name());
        myViewHolder.sale_price.setText(String.valueOf(product.getSale_price()));
        myViewHolder.tag_price.setText(String.valueOf(product.getTag_price()));
        myViewHolder.amount.setText(String.valueOf(product.getAmount()));
        myViewHolder.quantity.setText(String.valueOf(product.getQuantity()));
        if (product.getQuantity() != 0) {
            myViewHolder.plus_minus_holder.setVisibility(View.VISIBLE);
            myViewHolder.add_to_cart_button.setVisibility(View.GONE);
        }
        myViewHolder.tag_price.setText(String.valueOf(product.getTag_price()));
        Picasso.get()
                .load(product.getProduct_image())
                .resize(120, 80)
                .placeholder(R.drawable.simple_product)
                .error(R.drawable.simple_product)
                .centerCrop()
                .into(myViewHolder.product_image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ProductButtonClickListener {

        TextView product_name;
        TextView tag_price;
        TextView sale_price;
        TextView amount;
        ImageView product_image;
        LinearLayout plus_minus_holder;
        LinearLayout listi_item_container;
        ImageView increase_button;
        TextView quantity;
        ImageView decrease_button;
        Button add_to_cart_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Set on click listener to list items click event
            itemView.setOnClickListener(this);

            product_name = itemView.findViewById(R.id.product_name_id);
            tag_price = itemView.findViewById(R.id.tag_price_id);
            sale_price = itemView.findViewById(R.id.sale_price_id);
            product_image = itemView.findViewById(R.id.product_image_id);
            plus_minus_holder = itemView.findViewById(R.id.plus_minus_container_id);
            increase_button = itemView.findViewById(R.id.increase_button_id);
            quantity = itemView.findViewById(R.id.quantity_text_id);
            decrease_button = itemView.findViewById(R.id.decrease_button_id);
            add_to_cart_button = itemView.findViewById(R.id.product_list_add_to_cart_button_id);
            amount = itemView.findViewById(R.id.amount_text_id);
            listi_item_container = itemView.findViewById(R.id.product_list_item_container_id);

            add_to_cart_button.setOnClickListener(this);
            increase_button.setOnClickListener(this);
            decrease_button.setOnClickListener(this);
            listi_item_container.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //Get the position of the row clicked
            int position = getAdapterPosition();
            Product product = mData.get(position);


            switch (view.getId()) {
                case R.id.product_list_item_container_id:
                    ProductDetailsViewDialog pdv = new ProductDetailsViewDialog(mContext, product, MyViewHolder.this);
                    pdv.showDialog();
                    break;
                case R.id.product_list_add_to_cart_button_id:
                    addToCart(product);
                    productButtonClickListener.onAddToCartButtonClick(product);
                    break;
                case R.id.increase_button_id:
                    addToCart(product);
                    productButtonClickListener.onIncreaseButtonClick(product);
                    break;
                case R.id.decrease_button_id:
                    Cart cart = new Cart();
                    cart.removeItemFromCart(product);
                    productButtonClickListener.onDecreaseButtonClick(product);
                    Product p = cart.getCartItem(product.getId());
                    quantityCheck(product);

                    quantity.setText(String.valueOf(cart.getCartItem(product.getId()).getQuantity()));
                    amount.setText(String.valueOf(cart.getCartItem(product.getId()).getAmount()));
                    break;
            }

        }

        public void addToCart(Product product) {
            Cart cart = new Cart();
            cart.putItemToCart(product);

            quantityCheck(product);
            Product p = cart.getCartItem(product.getId());

            quantity.setText(String.valueOf(p.getQuantity()));
            amount.setText(String.valueOf(p.getAmount()));
        }

        @Override
        public void onAddToCartButtonClick(Product product) {
            int index = 0;
            for (Product item : GlobalData.cartItems) {
                if (product.getId() == item.getId()) {
                    GlobalData.cartItems.set(index, product);
                }
                index++;
            }
            notifyDataSetChanged();
            quantityCheck(product);
        }

        @Override
        public void onIncreaseButtonClick(Product product) {
            int index = 0;
            for (Product item : GlobalData.cartItems) {
                if (product.getId() == item.getId()) {
                    GlobalData.cartItems.set(index, product);
                }
                index++;
            }
            notifyDataSetChanged();
            quantityCheck(product);
        }

        @Override
        public void onDecreaseButtonClick(Product product) {
            int index = 0;
            for (Product item : GlobalData.cartItems) {
                if (product.getId() == item.getId()) {
                    GlobalData.cartItems.set(index, product);
                }
                index++;
            }
            notifyDataSetChanged();
            quantityCheck(product);
        }

        public void quantityCheck(Product product) {
            if (product.getQuantity() == 0) {
                add_to_cart_button.setVisibility(View.VISIBLE);
                plus_minus_holder.setVisibility(View.GONE);
            } else {
                add_to_cart_button.setVisibility(View.GONE);
                plus_minus_holder.setVisibility(View.VISIBLE);
            }
        }
    }

}
