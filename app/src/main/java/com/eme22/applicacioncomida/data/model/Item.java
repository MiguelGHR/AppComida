package com.eme22.applicacioncomida.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public final class Item {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("categoryId")
    @Expose
    private long categoryId;
    @SerializedName("name")
    @Expose
    @Nullable
    private String name;
    @SerializedName("description")
    @Expose
    @Nullable
    private String description;
    @SerializedName("price")
    @Expose
    @Nullable
    private Double price;
    @SerializedName("promoId")
    @Expose
    @Nullable
    private Long promoId;
    @Nullable
    private Promo promo;
    @SerializedName("image")
    @Expose
    @Nullable
    private String image;
    @SerializedName("createdAt")
    @Expose
    @Nullable
    private String createdAt;

    public Item(long id, long categoryId, @Nullable String name, @Nullable String description, @Nullable Double price, @Nullable Long promoId, @Nullable String image, @Nullable String createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.promoId = promoId;
        this.image = image;
        this.createdAt = createdAt;
    }

    public Item() {

    }

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public Double getPrice() {
        return price;
    }

    @Nullable
    public Long getPromoId() {
        return promoId;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }
    @Nullable
    public Promo getPromo() {
        return promo;
    }

    public void setPromo(@androidx.annotation.Nullable Promo promo) {
        this.promo = promo;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setPrice(@Nullable Double price) {
        this.price = price;
    }

    public void setPromoId(@Nullable Long promoId) {
        this.promoId = promoId;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }
}
