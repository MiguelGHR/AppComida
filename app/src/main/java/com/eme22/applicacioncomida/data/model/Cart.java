package com.eme22.applicacioncomida.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class Cart {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("user")
    @Expose
    @NotNull
    private User user;
    @SerializedName("userId")
    @Expose
    private long userId;
    @SerializedName("cartItems")
    @Expose
    private ArrayList<CartItem> cartItems;
    @SerializedName("finished")
    @Expose
    private boolean finished;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public Cart(long id, @NotNull User user, long userId, ArrayList<CartItem> cartItems, boolean finished, String createdAt) {
        this.id = id;
        this.user = user;
        this.userId = userId;
        this.cartItems = cartItems;
        this.finished = finished;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    @NotNull
    public User getUser() {
        return user;
    }

    public long getUserId() {
        return userId;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public boolean isFinished() {
        return finished;
    }

    @NotNull
    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setCreatedAt(@NotNull String createdAt) {
        this.createdAt = createdAt;
    }
}
