package com.eme22.applicacioncomida.ui.editor_item;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eme22.applicacioncomida.data.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryArrayAdapter extends ArrayAdapter<Category> {

    private final List<Category> mItemList;

    public CategoryArrayAdapter(@NonNull Context context, int resource, List<Category> itemList) {
        super(context, resource);
        this.mItemList = itemList;
    }

    public CategoryArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.mItemList = new ArrayList<>();
    }

    @Override
    public int getCount(){
        return this.mItemList.size();
    }

    @Override
    public Category getItem(int position){
        System.out.println(position);
        return this.mItemList.get(Math.min(position, mItemList.size()-1));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(mItemList.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(mItemList.get(position).getName());

        return label;
    }

    public int getAdapterPosition(int toIntExact) {
        int i;
        for (i = 0; i < mItemList.size(); i++) {
            Category category = mItemList.get(i);
            if (category.getId() == toIntExact)
                break;
        }
        return i;
    }
}
