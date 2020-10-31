package com.example.estoreclient.interfaces;

import com.example.estoreclient.models.Product;

public interface ProductButtonClickListener {
    void onAddToCartButtonClick(Product product);

    void onIncreaseButtonClick(Product product);

    void onDecreaseButtonClick(Product product);
}
