package com.glsi.atyourservice.models;

import com.glsi.atyourservice.room.BasketItem;

public class ProductBasketItem {
    private Product product;
    private BasketItem basketItem;

    public ProductBasketItem() {
    }

    public ProductBasketItem(Product product, BasketItem basketItem) {
        this.product = product;
        this.basketItem = basketItem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BasketItem getBasketItem() {
        return basketItem;
    }

    public void setBasketItem(BasketItem basketItem) {
        this.basketItem = basketItem;
    }
}
