package com.eme22.applicacioncomida.ui.buy;

import static com.eme22.applicacioncomida.data.api.WebApiAdapter.API_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.eme22.applicacioncomida.data.model.CartItem;
import com.eme22.applicacioncomida.databinding.FragmentBuyBinding;
import com.eme22.applicacioncomida.ui.cart.CartViewModel;
import com.eme22.applicacioncomida.ui.category_item.CategoryItem;
import com.eme22.applicacioncomida.ui.category_item.CategoryItemViewModel;
import com.eme22.applicacioncomida.ui.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.MessageFormat;

public class BuyFragment extends Fragment {

    private BuyViewModel mViewModel;
    private CategoryItemViewModel helperViewModel;
    private CartViewModel helperViewModel2;

    private FragmentBuyBinding binding;

    private final CategoryItem categoryItem;

    private BuyFragment(CategoryItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public static BuyFragment newInstance(CategoryItem categoryItem) {
        return new BuyFragment(categoryItem);
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBuyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initView();
        initData();

        return view;
    }

    private void initView(){
        mViewModel = new ViewModelProvider(this).get(BuyViewModel.class);
        helperViewModel = new ViewModelProvider(categoryItem).get(CategoryItemViewModel.class);
        helperViewModel2 = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        binding.fragmentBuyCardView.setVisibility(View.GONE);

        binding.fragmentBuyLess.setOnClickListener(v -> mViewModel.removeItem());

        binding.fragmentBuyMore.setOnClickListener(v -> mViewModel.addItem());

        binding.fragmentBuyAccept.setOnClickListener(v -> addToCartCurrent());

        binding.fragmentBuyCancel.setOnClickListener(v -> requireActivity().onBackPressed());

    }

    private void addToCartCurrent() {
        helperViewModel2.addItem(new CartItem(0, null, 0, mViewModel.getCurrent().getValue().getId(), mViewModel.getCurrent().getValue(),mViewModel.getCount().getValue()), ((MainActivity) requireActivity()).getUser());
        ((MainActivity) requireActivity()).buySuccess() ;
    }

    private void initData() {

        mViewModel.getCurrent().observe(getViewLifecycleOwner(), item -> {

            binding.fragmentBuyTitle.setText(item.getName());
            binding.fragmentBuyDescription.setText(item.getDescription());
            Picasso.get().load(API_URL + item.getImage()).into(binding.fragmentBuyItemImage);
            binding.fragmentBuyCardView.setVisibility(View.VISIBLE);

        });

        mViewModel.setCurrent(helperViewModel.getSelected().getValue());

        mViewModel.getPrice().observe(getViewLifecycleOwner(), aDouble -> binding.fragmentBuyPrice.setText(df.format(aDouble)));

        mViewModel.getCount().observe(getViewLifecycleOwner(), number -> {

            binding.fragmentBuyLess.setClickable(number >= 2);
            binding.fragmentBuyCount.setText(MessageFormat.format("{0}", number));
        });

    }
}