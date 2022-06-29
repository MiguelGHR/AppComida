package com.eme22.applicacioncomida.ui.editor_item;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.Promo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PromoArrayAdapter extends ArrayAdapter<Promo> {

    private final ArrayList<Promo> mItemList = new ArrayList<>(Collections.singletonList(new Promo()));

    public PromoArrayAdapter(@NonNull Context context, int resource, List<Promo> itemList) {
        super(context, resource);
        this.mItemList.addAll(itemList);
    }

    public PromoArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount(){
        return mItemList.size();
    }

    @Override
    public Promo getItem(int position){
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(mItemList.get(position).getName() == null ? "Ninguno" : mItemList.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(mItemList.get(position).getName() == null ? "Ninguno" : mItemList.get(position).getName());

        return label;
    }
}
