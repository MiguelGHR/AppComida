package com.eme22.applicacioncomida.ui.category;

import static com.eme22.applicacioncomida.data.api.WebApiAdapter.API_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder> {

    private final CategoryAdapter.OnItemClicked listener;
    protected ArrayList<Category> mItemList;

    public void addAll(List<Category> mcList) {
        for (Category mc : mcList) {
            add(mc);
        }
    }

    public void add(Category mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void remove(Category city) {
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

    public Category getItem(int position) {
        return mItemList.get(position);
    }


    public CategoryAdapter(CategoryAdapter.OnItemClicked listener) {
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.CategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryItemViewHolder holder, int position) {
        Category item = getItem(position);
        holder.title.setText(item.getName());
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.image.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Picasso.get().load(API_URL + item.getImage()).placeholder(circularProgressDrawable).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public interface OnItemClicked {
        void onItemClick(Category cartItem);
    }

    protected class CategoryItemViewHolder extends RecyclerView.ViewHolder {


        ImageView image = itemView.findViewById(R.id.cartItemImage);
        TextView title = itemView.findViewById(R.id.cartItemName);

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

