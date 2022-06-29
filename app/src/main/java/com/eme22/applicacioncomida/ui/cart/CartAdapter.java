package com.eme22.applicacioncomida.ui.cart;

import static com.eme22.applicacioncomida.data.api.WebApiAdapter.API_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.CartItem;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {

    private final OnItemClicked listener;
    protected ArrayList<CartItem> mItemList;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void addAll(List< CartItem > mcList) {
        for (CartItem mc: mcList) {
            add(mc);
        }
    }

    public void add(CartItem mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void remove(CartItem city) {
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

    public CartItem getItem(int position) {
        return mItemList.get(position);
    }


    public CartAdapter(OnItemClicked listener) {
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        CartItem item = getItem(position);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.image.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Picasso.get().load(API_URL + item.getItem().getImage()).placeholder(circularProgressDrawable).into(holder.image);
        holder.title.setText(item.getItem().getName());
        holder.count.setText(MessageFormat.format("Items: {0}", Math.toIntExact(item.getCount())));
        holder.price.setText(MessageFormat.format("{0} {1}", holder.price.getContext().getString(R.string.currency), df.format(item.getItem().getPrice())));

    }
    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);
    }

    protected class CartItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image = itemView.findViewById(R.id.cartItemImage);
        TextView title = itemView.findViewById(R.id.cartItemName);
        TextView price   = itemView.findViewById(R.id.cartItemPrice);
        TextView count  = itemView.findViewById(R.id.cartItemCount);
        ImageButton delete =  itemView.findViewById(R.id.cartItemDelete);


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            delete.setOnClickListener(v -> listener.onItemClick(getItem(getAdapterPosition())));
        }
    }

    public interface OnItemClicked {
        void onItemClick(CartItem cartItem);
    }
}
