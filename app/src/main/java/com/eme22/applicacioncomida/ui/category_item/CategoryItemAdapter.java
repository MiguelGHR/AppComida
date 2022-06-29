package com.eme22.applicacioncomida.ui.category_item;

import static com.eme22.applicacioncomida.data.api.WebApiAdapter.API_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Item;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder> {

    private final CategoryItemAdapter.OnItemClicked listener;
    protected ArrayList<Item> mItemList;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void addAll(List<Item> mcList) {
        for (Item mc : mcList) {
            add(mc);
        }
    }

    public void add(Item mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void remove(Item city) {
        int position = mItemList.indexOf(city);
        if (position > -1) {
            mItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public Item getItem(int position) {
        return mItemList.get(position);
    }


    public CategoryItemAdapter(CategoryItemAdapter.OnItemClicked listener) {
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryItemAdapter.CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new CategoryItemAdapter.CategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemAdapter.CategoryItemViewHolder holder, int position) {
        Item item = getItem(position);
        holder.title.setText(item.getName());
        holder.price.setText(generateItemPrice(item.getPrice(), item.getPromo() != null ? item.getPromo().getDiscount() : 0));
        Picasso.get().load(API_URL + item.getImage()).into(holder.image);
    }

    private String generateItemPrice(Double price, double v) {
        price = price - ( price * v);
        return "S/. "+df.format(price);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public interface OnItemClicked {
        void onItemClick(Item cartItem);
    }

    protected class CategoryItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image = itemView.findViewById(R.id.item_image);
        TextView title = itemView.findViewById(R.id.item_title);
        TextView price = itemView.findViewById(R.id.item_price);

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(mItemList.get(position));
                }
            });
        }
    }
}

