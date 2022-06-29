package com.eme22.applicacioncomida.ui.user_history;

import static com.eme22.applicacioncomida.data.api.WebApiAdapter.API_URL;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Cart;
import com.eme22.applicacioncomida.data.model.CartItem;
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.ui.editor.EditorFragmentAdapter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.EditorFragmentViewHolder> {

    protected ArrayList<Cart> mItemList = new ArrayList<>();

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void addAll(List<Cart> mcList) {
        for (Cart mc : mcList) {
            add(mc);
        }
    }

    public void add(Cart mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void remove(Cart city) {
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

    public Cart getItem(int position) {
        return mItemList.get(position);
    }


    @NonNull
    @Override
    public EditorFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_item , parent, false);
        return new EditorFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditorFragmentViewHolder holder, int position) {
        Cart item = getItem(position);

        holder.property2.setText(MessageFormat.format("Fecha: {0}", item.getCreatedAt()));
        holder.property1.setText(MessageFormat.format("Precio: {0}", calculaTePrice(item.getCartItems())));
        holder.property3.setText(MessageFormat.format("Items: {0}", calculateItems(item.getCartItems())));
        holder.property4.setVisibility(View.GONE);
        holder.property5.setVisibility(View.GONE);
        holder.property6.setVisibility(View.GONE);
        holder.property7.setVisibility(View.GONE);
        holder.imageView.setVisibility(View.GONE);
    }

    private String calculaTePrice(ArrayList<CartItem> cartItems) {
        double price = 0;
        for (CartItem item: cartItems) {
            if (item.getItem() != null)
                price += (item.getItem().getPrice() - ((item.getItem().getPromo() != null ? item.getItem().getPromo().getDiscount() : 0) * item.getItem().getPrice()))* item.getCount();
        }

        return df.format(price);
    }

    private String calculateItems(ArrayList<CartItem> cartItems) {

        StringBuilder sb = new StringBuilder();

        for (CartItem item: cartItems) {
            if (item.getItem() != null)
                sb.append(item.getItem().getName()).append(" ");
        }

        return sb.substring(0, Math.min(sb.length(), 30)) + "...";
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    protected class EditorFragmentViewHolder extends RecyclerView.ViewHolder {

        TextView property1 = itemView.findViewById(R.id.editor_item_property1);
        TextView property2 = itemView.findViewById(R.id.editor_item_property2);
        TextView property3 = itemView.findViewById(R.id.editor_item_property3);
        TextView property4 = itemView.findViewById(R.id.editor_item_property4);
        TextView property5 = itemView.findViewById(R.id.editor_item_property5);
        TextView property6 = itemView.findViewById(R.id.editor_item_property6);
        TextView property7 = itemView.findViewById(R.id.editor_item_property7);

        ImageView imageView = itemView.findViewById(R.id.editor_item_image);
        //ImageButton editButton = itemView.findViewById(R.id.editor_item_edit);
        //ImageButton deleteButton = itemView.findViewById(R.id.editor_item_delete);

        public EditorFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
