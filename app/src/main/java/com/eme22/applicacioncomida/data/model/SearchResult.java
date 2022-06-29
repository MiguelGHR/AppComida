package com.eme22.applicacioncomida.data.model;

import android.os.Parcel;

import com.eme22.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult implements SearchSuggestion {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("image")
    @Expose
    public String image;

    public final static Creator<SearchResult> CREATOR = new Creator<SearchResult>() {


        public SearchResult createFromParcel(android.os.Parcel in) {
            return new SearchResult(in);
        }

        public SearchResult[] newArray(int size) {
            return (new SearchResult[size]);
        }

    };

    public SearchResult(String name, String category, long id, String image) {
        this.name = name;
        this.category = category;
        this.id = id;
        this.image = image;
    }

    public SearchResult() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getBody() {
        return getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public SearchResult[] newArray(int size) {
        return (new SearchResult[size]);
    }


    protected SearchResult(android.os.Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.category = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((long) in.readValue((long.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(category);
        dest.writeValue(id);
        dest.writeValue(image);
    }
}
