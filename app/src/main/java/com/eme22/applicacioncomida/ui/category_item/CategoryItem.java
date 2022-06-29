package com.eme22.applicacioncomida.ui.category_item;

import static com.eme22.applicacioncomida.util.Util.getColor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.databinding.FragmentCategoryItemBinding;
import com.eme22.applicacioncomida.ui.buy.BuyFragment;
import com.eme22.applicacioncomida.ui.category.CategoryViewModel;

public class CategoryItem extends Fragment {

    private CategoryItemViewModel mViewModel;
    private CategoryViewModel helperViewModel;

    private FragmentCategoryItemBinding binding;
    private CategoryItemAdapter adapter;

    public static CategoryItem newInstance() {
        return new CategoryItem();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCategoryItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initView();
        initData();

        return view;
    }


    private void initView() {
        mViewModel = new ViewModelProvider(this).get(CategoryItemViewModel.class);
        helperViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        final Drawable upArrow = ContextCompat.getDrawable(requireContext(),com.eme22.floatingsearchview.R.drawable.ic_arrow_back_black_24dp);
        if (upArrow != null) {
            upArrow.setColorFilter(getColor(com.google.android.material.R.attr.colorOnPrimary, requireContext()), PorterDuff.Mode.SRC_ATOP);
            binding.categoryItemFragmentToolbar.setNavigationIcon(upArrow);
        }


        binding.categoryItemFragmentToolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        binding.categoryFragmentRecyler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new CategoryItemAdapter(this::buyItem);
        binding.categoryFragmentRecyler.setAdapter(adapter);
    }

    private void buyItem(Item cartItem) {
        mViewModel.setSelected(cartItem);
        FragmentTransaction fTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.add(R.id.main_fragment, BuyFragment.newInstance(this), "BuyItem");
        fTransaction.commit();
    }

    private void initData() {

        mViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            adapter.clear();
            adapter.addAll(items);

            binding.categoryItemProgressBar.setVisibility(View.GONE);
            binding.categoryFragmentRecyler.setVisibility(View.VISIBLE);

            if (items.size() == 0)
                binding.catergoryNoItems.setVisibility(View.VISIBLE);


        });

        mViewModel.retrieveCategoryItems(Math.toIntExact(helperViewModel.getSelected().getValue().getId()));


    }



}