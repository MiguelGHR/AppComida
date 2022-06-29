package com.eme22.applicacioncomida.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public final class CartItem {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("cart")
    @Expose
    @Nullable
    private String cart;
    @SerializedName("cartId")
    @Expose
    private long cartId;
    @SerializedName("itemId")
    @Expose
    private long itemId;
    private transient Item item;
    @SerializedName("count")
    @Expose
    private long count;

    public CartItem(long id, @Nullable String cart, long cartId, long itemId, long count) {
        this.id = id;
        this.cart = cart;
        this.cartId = cartId;
        this.itemId = itemId;
        this.count = count;
        this.item = null;
    }

    public CartItem(long id, @Nullable String cart, long cartId, long itemId, Item item, long count) {
        this.id = id;
        this.cart = cart;
        this.cartId = cartId;
        this.itemId = itemId;
        this.item = item;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public String getCart() {
        return cart;
    }

    public void setCart(@Nullable String cart) {
        this.cart = cart;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

