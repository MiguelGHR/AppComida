package com.eme22.applicacioncomida.ui.home;

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
import com.eme22.applicacioncomida.data.model.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchResultItemViewHolder> {

    private final SearchAdapter.OnItemClicked listener;
    protected ArrayList<SearchResult> mItemList;

    public void addAll(List<SearchResult> mcList) {
        for (SearchResult mc : mcList) {
            add(mc);
        }
    }

    public void add(SearchResult mc) {
        mItemList.add(mc);
        notifyItemInserted(mItemList.size() - 1);
    }

    public void remove(SearchResult city) {
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

    public SearchResult getItem(int position) {
        return mItemList.get(position);
    }


    public SearchAdapter(SearchAdapter.OnItemClicked listener) {
        this.listener = listener;
        this.mItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchResultItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_search_result_item, parent, false);
        return new SearchAdapter.SearchResultItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultItemViewHolder holder, int position) {
        SearchResult item = getItem(position);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.image.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Picasso.get().load(API_URL + item.getImage()).placeholder(circularProgressDrawable).into(holder.image);
        holder.text.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public interface OnItemClicked {
        void onItemClick(SearchResult cartItem);
    }

    protected class SearchResultItemViewHolder extends RecyclerView.ViewHolder {


        ImageView image = itemView.findViewById(R.id.home_search_result_image);
        TextView text = itemView.findViewById(R.id.home_search_result_text);

        public SearchResultItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }
}

