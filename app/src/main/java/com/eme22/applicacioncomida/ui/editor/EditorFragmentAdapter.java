package com.eme22.applicacioncomida.ui.editor;

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
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.User;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

public class EditorFragmentAdapter extends RecyclerView.Adapter<EditorFragmentAdapter.EditorFragmentViewHolder>{

    private final EditorFragmentAdapter.OnItemClicked listener;
    private ArrayList<Object> mItemList;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public EditorFragmentAdapter(OnItemClicked listener) {
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    public void addAll(Collection mcList) {
        for (Object mc : mcList) {
            add(mc);
        }
    }

    public void add(Object mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void remove(Object city) {
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

    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    @NonNull
    @Override
    public EditorFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_item , parent, false);
        return new EditorFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditorFragmentViewHolder holder, int position) {
        Object item = getItem(position);
        if (item instanceof Item) {
            holder.property2.setText(MessageFormat.format("Id: {0}", Math.toIntExact(((Item) item).getId())));
            holder.property1.setText(MessageFormat.format("Categoria: {0}", Math.toIntExact(((Item) item).getCategoryId())));
            holder.property3.setText(MessageFormat.format("Nombre: {0}", ((Item) item).getName()));
            holder.property4.setText(MessageFormat.format("Description: {0}", ((Item) item).getDescription()));
            holder.property5.setText(MessageFormat.format("Precio: S/. {0}", df.format(((Item) item).getPrice())));
            if (((Item) item).getPromoId() != null)
                holder.property6.setText(MessageFormat.format("Promocion: {0}", Math.toIntExact(((Item) item).getPromoId())));
            else
                holder.property6.setVisibility(View.GONE);
            holder.property7.setVisibility(View.GONE);
            Picasso.get().load(API_URL + ((Item) item).getImage()).into(holder.imageView);
        }
        else if (item instanceof Category) {
            holder.property2.setText(MessageFormat.format("Id: {0}", Math.toIntExact(((Category) item).getId())));
            holder.property1.setText(MessageFormat.format("Nombre: {0}", ((Category) item).getName()));
            holder.property3.setText(MessageFormat.format("Description: {0}", ((Category) item).getDescription()));
            holder.property4.setVisibility(View.GONE);
            holder.property5.setVisibility(View.GONE);
            holder.property6.setVisibility(View.GONE);
            holder.property7.setVisibility(View.GONE);
            Picasso.get().load(API_URL + ((Category) item).getImage()).into(holder.imageView);
        }
        else if (item instanceof Promo) {
            holder.property2.setText(MessageFormat.format("Id: {0}", Math.toIntExact(((Promo) item).getId())));
            holder.property1.setText(MessageFormat.format("Nombre: {0}", ((Promo) item).getName()));
            holder.property3.setText(MessageFormat.format("Description: {0}", ((Promo) item).getDescription()));
            holder.property4.setText(MessageFormat.format("Descuento: S/. {0}", df.format(((Promo) item).getDiscount())));
            holder.property5.setVisibility(View.GONE);
            holder.property6.setVisibility(View.GONE);
            holder.property7.setVisibility(View.GONE);
            Picasso.get().load(API_URL + ((Promo) item).getImage()).into(holder.imageView);
        }
        else if (item instanceof User) {
            holder.property2.setText(MessageFormat.format("Id: {0}", Math.toIntExact(((User) item).getId())));
            holder.property1.setText(MessageFormat.format("Nombre: {0}", ((User) item).getFirstName()));
            holder.property3.setText(MessageFormat.format("Apellido: {0}", ((User) item).getLastName()));
            holder.property4.setText(MessageFormat.format("Direccion: {0}", ((User) item).getAddress()));
            holder.property5.setText(MessageFormat.format("Phone: {0}", ((User) item).getPhone()));
            holder.property6.setText(MessageFormat.format("Correo: {0}", ((User) item).getEmail()));
            holder.property7.setText(MessageFormat.format("Admin: {0}", ((User) item).isAdmin()));

            Picasso.get().load(API_URL + ((User) item).getImage()).error(R.drawable.ic_user_2).into(holder.imageView);
        }
    }

    public interface OnItemClicked {
        void onEditClicked(Object item);
        void onDeleteClicked(Object item);
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
        ImageButton editButton = itemView.findViewById(R.id.editor_item_edit);
        ImageButton deleteButton = itemView.findViewById(R.id.editor_item_delete);

        public EditorFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton.setOnClickListener(v -> listener.onEditClicked(getItem(getAdapterPosition())));
            deleteButton.setOnClickListener(v -> listener.onDeleteClicked(getItem(getAdapterPosition())));
        }
    }
}
