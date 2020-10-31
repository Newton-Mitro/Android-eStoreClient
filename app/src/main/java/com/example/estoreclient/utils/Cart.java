package com.example.estoreclient.utils;


import com.example.estoreclient.models.Product;

public class Cart {

    public Cart() {

    }

    public Product getCartItem(int product_id) {
        Product product = new Product();
        for (Product item : GlobalData.cartItems) {
            if (product_id == item.getId()) {
                product = item;
            }
        }
        return product;
    }

    public void putItemToCart(Product cartItem) {
        boolean notfound = true;
        if (GlobalData.cartItems.size()!=0) {
            int index = 0;
            for (Product item : GlobalData.cartItems) {
                if (cartItem.getId() == item.getId()) {
                    cartItem.setQuantity(item.getQuantity() + 1);
                    cartItem.setAmount(item.getQuantity()*item.getSale_price());
                    GlobalData.cartItems.set(index,cartItem);
                    notfound = false;
                }
                index++;
            }
            if (notfound){
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setAmount(cartItem.getQuantity()*cartItem.getSale_price());
                GlobalData.cartItems.add(cartItem);
            }
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setAmount(cartItem.getQuantity()*cartItem.getSale_price());
            GlobalData.cartItems.add(cartItem);
        }

    }

    public void removeItemFromCart(Product cartItem) {
        if (GlobalData.cartItems.size()!=0) {
            int index = 0;
            for (Product item : GlobalData.cartItems) {
                if (cartItem.getId() == item.getId()) {
                    if (cartItem.getQuantity()==1){
                        cartItem.setQuantity(item.getQuantity() - 1);
                        cartItem.setAmount(item.getQuantity()*item.getSale_price());
                        GlobalData.cartItems.remove(index);
                        break;
                    }else {
                        cartItem.setQuantity(item.getQuantity() - 1);
                        cartItem.setAmount(item.getQuantity()*item.getSale_price());
                        GlobalData.cartItems.set(index,cartItem);
                        break;
                    }
                }
                index++;
            }
        }
    }

    public double getCartTotal(){
        double total = 0;
        for (Product item : GlobalData.cartItems) {
            total = (total + item.getAmount());
        }
        return total;
    }
}
