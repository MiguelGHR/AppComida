package com.eme22.applicacioncomida.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.ui.cart.CartFragment;
import com.eme22.applicacioncomida.ui.category.CategoryFragment;
import com.eme22.applicacioncomida.ui.home.HomeFragment;

public class MainStateAdapter extends FragmentStateAdapter {

    private final FragmentActivity fragmentActivity;

    public MainStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }

    public String getTitle(int position) {
        switch(position) {
            case 0:
                return this.fragmentActivity.getString(R.string.home);
            case 1:
                return this.fragmentActivity.getString(R.string.category);
            case 2:
                return this.fragmentActivity.getString(R.string.cart);
            default:
                return null;
        }
    }

    public View getTabView(int i) {
        View v = LayoutInflater.from(fragmentActivity).inflate(R.layout.tab_item, null);
        TextView tv = v.findViewById(R.id.textView3);
        tv.setText(getTitle(i));
        ImageView img  = v.findViewById(R.id.imageView3);
        img.setImageResource(getImage(i));
        return v;
    }

    private int getImage(int i) {
        switch(i) {
            case 0:
                return R.drawable.ic_home;
            case 1:
                return R.drawable.ic_food;
            case 2:
                return R.drawable.ic_cart;
            default:
                throw new RuntimeException();
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return  HomeFragment.newInstance();
            case 1:
                return CategoryFragment.newInstance();
            case 2:
                return CartFragment.newInstance();
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
