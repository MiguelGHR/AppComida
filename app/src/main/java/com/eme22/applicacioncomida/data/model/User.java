package com.eme22.applicacioncomida.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("email")
    @Expose
    @Nullable
    private String email;
    @SerializedName("passwordHash")
    @Expose
    @Nullable
    private String passwordHash;
    @SerializedName("firstName")
    @Expose
    @Nullable
    private String firstName;
    @SerializedName("lastName")
    @Expose
    @Nullable
    private String lastName;
    @SerializedName("address")
    @Expose
    @Nullable
    private String address;
    @SerializedName("phone")
    @Expose
    @Nullable
    private Long phone;
    @SerializedName("admin")
    @Expose
    private boolean admin;
    @SerializedName("image")
    @Expose
    @Nullable
    private String image;
    @SerializedName("createdAt")
    @Expose
    @Nullable
    private String createdAt;

    public User() {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.admin = admin;
        this.image = image;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getPasswordHash() {
        return passwordHash;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @Nullable
    public Long getPhone() {
        return phone;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public void setPasswordHash(@Nullable String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    public void setPhone(@Nullable Long phone) {
        this.phone = phone;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }
}
