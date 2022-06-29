package com.eme22.applicacioncomida.ui.user_history;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Cart;
import com.eme22.applicacioncomida.data.model.User;
import com.eme22.applicacioncomida.databinding.FragmentUserHistoryBinding;
import com.eme22.applicacioncomida.ui.category.CategoryAdapter;

import java.util.ArrayList;

public class UserHistoryFragment extends Fragment {

    private UserHistoryViewModel mViewModel;
    private FragmentUserHistoryBinding binding;
    private UserHistoryAdapter adapter;

    private final User user;

    private UserHistoryFragment(User user) {
        this.user = user;
    }

    public static UserHistoryFragment newInstance(User user) {
        return new UserHistoryFragment(user);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initView();
        initData();

        return view;
    }


    private void initView() {

        adapter = new UserHistoryAdapter();
        binding.userHistoryFragmentRecyler.setAdapter(adapter);
    }

    private void initData() {
        mViewModel = new ViewModelProvider(this).get(UserHistoryViewModel.class);

        mViewModel.getCarts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Cart>>() {
            @Override
            public void onChanged(ArrayList<Cart> carts) {
                if (carts.size() == 0)
                    binding.userHistoryFragmentNoItems.setVisibility(View.VISIBLE);
                else
                    binding.userHistoryFragmentNoItems.setVisibility(View.GONE);


                adapter.addAll(carts);
            }
        });

        mViewModel.retrieveCarts(Math.toIntExact(user.getId()));

    }

}