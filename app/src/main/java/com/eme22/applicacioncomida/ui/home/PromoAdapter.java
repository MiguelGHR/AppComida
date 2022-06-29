package com.eme22.applicacioncomida.ui.home;

import static com.eme22.applicacioncomida.data.api.WebApiAdapter.API_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Promo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoItemViewHolder> {

    private final PromoAdapter.OnItemClicked listener;
    protected ArrayList<Promo> mItemList;

    public void addAll(List<Promo> mcList) {
        for (Promo mc : mcList) {
            add(mc);
        }
    }

    public void add(Promo mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void remove(Promo city) {
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

    public Promo getItem(int position) {
        return mItemList.get(position % mItemList.size());
    }


    public PromoAdapter(PromoAdapter.OnItemClicked listener) {
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PromoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_promotion_item, parent, false);
        return new PromoAdapter.PromoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoItemViewHolder holder, int position) {
        Promo item = getItem(position);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.image.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Picasso.get().load(API_URL + item.getImage()).placeholder(circularProgressDrawable).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (mItemList.size() != 0)
            return Integer.MAX_VALUE;
        return 0;
    }

    public interface OnItemClicked {
        void onItemClick(Promo cartItem);
    }

    protected class PromoItemViewHolder extends RecyclerView.ViewHolder {


        ImageView image = itemView.findViewById(R.id.item_image);

        public PromoItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }
}

