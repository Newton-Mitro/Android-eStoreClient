package com.example.estoreclient.utils.dialogboxes;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.estoreclient.R;
import com.example.estoreclient.interfaces.ProductButtonClickListener;
import com.example.estoreclient.models.Product;
import com.example.estoreclient.utils.Cart;
import com.squareup.picasso.Picasso;

public class ProductDetailsViewDialog implements View.OnClickListener {
    private Context context;
    private Button add_to_cart_button;
    private ImageView increase_button;
    private ImageView dialog_close;
    private ImageView decrease_button;
    private Product product;
    private ImageView product_image;
    private TextView quantity_text;
    private TextView amount;
    private TextView product_name;
    private TextView category_name;
    private TextView brand_name;
    private TextView tag_price;
    private TextView sale_price;
    private TextView product_logn_description;
    private LinearLayout plus_minus_container;
    private Dialog productDetails;
    private ProductButtonClickListener productButtonClickListener;

    public ProductDetailsViewDialog(Context context, Product product,ProductButtonClickListener productButtonClickListener) {
        this.product = product;
        this.context = context;
        this.productButtonClickListener = productButtonClickListener;
    }

    public void showDialog() {
        productDetails = new Dialog(context);
        productDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
        productDetails.setContentView(R.layout.single_product_layout);
        productDetails.setCancelable(true);
        final WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        lp1.copyFrom(productDetails.getWindow().getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.MATCH_PARENT;
        productDetails.show();
        productDetails.getWindow().setAttributes(lp1);
        dialog_close = productDetails.findViewById(R.id.product_description_close_id);
        product_name = productDetails.findViewById(R.id.product_name_id);
        category_name = productDetails.findViewById(R.id.category_name_id);
        product_image = productDetails.findViewById(R.id.product_image_id);
        brand_name = productDetails.findViewById(R.id.brand_name_id);
        product_logn_description = productDetails.findViewById(R.id.product_long_description_id);
        tag_price = productDetails.findViewById(R.id.tag_price_id);
        sale_price = productDetails.findViewById(R.id.sale_price_id);
        increase_button = productDetails.findViewById(R.id.increase_button_id);
        decrease_button = productDetails.findViewById(R.id.decrease_button_id);
        add_to_cart_button = productDetails.findViewById(R.id.add_to_cart_button_id);
        amount = productDetails.findViewById(R.id.amount_text_id);
        quantity_text = productDetails.findViewById(R.id.quantity_text_id);
        plus_minus_container = productDetails.findViewById(R.id.plus_minus_container_id);

        Cart cart = new Cart();
        if (cart.getCartItem(product.getId()).getProduct_name()!=null){
            product = cart.getCartItem(product.getId());
        }

        Picasso.get()
                .load(product.getProduct_image())
                .resize(300, 280)
                .placeholder(R.drawable.not_found)
                .error(R.drawable.not_found)
                .centerCrop()
                .into(product_image);
        product_name.setText(product.getProduct_name());
        category_name.setText(product.getCategory_name());
        brand_name.setText(product.getBrand_name());
        product_logn_description.setText(product.getProduct_long_description());
        tag_price.setText(String.valueOf(product.getTag_price()));
        sale_price.setText(String.valueOf(product.getSale_price()));
        amount.setText(String.valueOf(product.getAmount()));
        quantity_text.setText(String.valueOf(product.getQuantity()));
        if (product.getQuantity()!=0){
            plus_minus_container.setVisibility(View.VISIBLE);
            add_to_cart_button.setVisibility(View.GONE);
        }

        dialog_close.setOnClickListener(this);
        add_to_cart_button.setOnClickListener(this);
        increase_button.setOnClickListener(this);
        decrease_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.product_description_close_id:
                productDetails.cancel();
                break;
            case R.id.add_to_cart_button_id:
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
                if (product.getQuantity()!=0){
                    add_to_cart_button.setVisibility(View.GONE);
                    plus_minus_container.setVisibility(View.VISIBLE);
                }else {
                    add_to_cart_button.setVisibility(View.VISIBLE);
                    plus_minus_container.setVisibility(View.GONE);
                }
                quantity_text.setText(String.valueOf(cart.getCartItem(product.getId()).getQuantity()));
                amount.setText(String.valueOf(cart.getCartItem(product.getId()).getAmount()));
                break;
        }
    }

    public void addToCart(Product product){
        Cart cart = new Cart();
        cart.putItemToCart(product);
        if (product.getQuantity()!=0){
            add_to_cart_button.setVisibility(View.GONE);
            plus_minus_container.setVisibility(View.VISIBLE);
        }else {
            add_to_cart_button.setVisibility(View.INVISIBLE);
            plus_minus_container.setVisibility(View.GONE);
        }

        quantity_text.setText(String.valueOf(cart.getCartItem(product.getId()).getQuantity()));
        amount.setText(String.valueOf(cart.getCartItem(product.getId()).getAmount()));
    }
}
