package com.glsi.atyourservice.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "basket_item")
public class BasketItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "product_id")
    private String productId;
    @ColumnInfo(name = "quantity")
    private int quantity;

    public BasketItem() {
    }

    public BasketItem(String productId, int quantity) {
//        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
