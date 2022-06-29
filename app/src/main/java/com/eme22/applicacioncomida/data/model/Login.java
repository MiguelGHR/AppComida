package com.eme22.applicacioncomida.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;


public final class Login {
    @SerializedName("exist")
    @Expose
    @Nullable
    private Boolean exist;
    @SerializedName("count")
    @Expose
    @Nullable
    private Integer count;

    public Login(@Nullable Boolean exist, @Nullable Integer count) {
        this.exist = exist;
        this.count = count;
    }

    @Nullable
    public Boolean getExist() {
        return exist;
    }

    @Nullable
    public Integer getCount() {
        return count;
    }
}
