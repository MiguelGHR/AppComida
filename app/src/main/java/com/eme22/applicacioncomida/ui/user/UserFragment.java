package com.eme22.applicacioncomida.ui.user;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.User;
import com.eme22.applicacioncomida.databinding.FragmentUserBinding;
import com.eme22.applicacioncomida.ui.admin.AdminActivity;
import com.eme22.applicacioncomida.ui.editor_item.EditorItemFragment;
import com.eme22.applicacioncomida.ui.main.MainActivity;
import com.eme22.applicacioncomida.ui.user_history.UserHistoryFragment;

public class UserFragment extends Fragment {

    private UserViewModel mViewModel;
    private FragmentUserBinding binding;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initView();
        initData();

        return view;
    }

    private void initView() {

        binding.fragmentUserName.setText(((MainActivity) requireActivity()).getUser() .getFirstName());

        mViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding.fragmentUserEdit.setOnClickListener(v -> editSelfUserData());
        binding.fragmentUserHistory.setOnClickListener(v -> openUserHistory());



    }

    private void openUserHistory() {
        FragmentTransaction fTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.replace(R.id.main_fragment, UserHistoryFragment.newInstance(mViewModel.getCurrent().getValue()), "UserHistoryFragment");
        fTransaction.commit();
    }

    private void editSelfUserData() {
        FragmentTransaction fTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.replace(R.id.main_fragment, EditorItemFragment.newInstance(mViewModel.getCurrent().getValue()), "EditorItemFragment");
        fTransaction.commit();
    }

    private void checkUserAdminFragment(User user) {

        binding.toolbar2.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        if (user.isAdmin()) {
            binding.fragmentUserAdminButton.setVisibility(View.VISIBLE);
            binding.fragmentUserAdminButton.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), AdminActivity.class);
                startActivity(intent);
            });
            return;
        }

        binding.fragmentUserAdminButton.setVisibility(View.GONE);
    }

    private void initData() {

        mViewModel.setCurrent(((MainActivity) requireActivity()).getUser());

        mViewModel.getCurrent().observe(getViewLifecycleOwner(), this::checkUserAdminFragment);

    }

}