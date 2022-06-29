package com.eme22.applicacioncomida.ui.editor;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.Category;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.User;
import com.eme22.applicacioncomida.databinding.FragmentCartBinding;
import com.eme22.applicacioncomida.databinding.FragmentEditorBinding;
import com.eme22.applicacioncomida.ui.admin.AdminActivity;
import com.eme22.applicacioncomida.ui.editor_item.EditorItemFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditorFragment extends Fragment {

    public static final int ITEM_EDITOR = 0;
    public static final int CATEGORY_EDITOR = 1;
    public static final int PROMO_EDITOR = 2;
    public static final int USER_EDITOR = 3;



    private final Integer CURRENT_EDITOR;
    private FragmentEditorBinding binding;
    private EditorFragmentAdapter adapter;

    private EditorFragment(int type) {
        CURRENT_EDITOR = type;
    }

    private EditorViewModel mViewModel;

    public static EditorFragment newInstance(int type) {
        return new EditorFragment(type);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEditorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        initView();
        initData();

        return view;

    }


    private void initView() {
        mViewModel = new ViewModelProvider(requireActivity()).get(EditorViewModel.class);

        adapter = new EditorFragmentAdapter(new EditorFragmentAdapter.OnItemClicked() {
            @Override
            public void onEditClicked(Object item) {
                editItem(item);
            }

            @Override
            public void onDeleteClicked(Object item) {
                deleteItem(item);
            }
        }
        );
        binding.fragmentEditorRecycler.setAdapter(adapter);

        binding.fragmentEditorRefresh.setOnRefreshListener(this::onDataChanged);

    }

    private void editItem(Object item) {
        ((AdminActivity) requireActivity()).fixFab();
        FragmentTransaction fTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.replace(R.id.activity_admin_main_fragment, EditorItemFragment.newInstance(item), "EditorItemFragment");
        fTransaction.commit();
    }

    private void deleteItem(Object item) {
        new AlertDialog.Builder(requireContext())
                .setMessage("¿Estas seguro de borrar este elemento?")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    mViewModel.deleteItem(item, new EditorViewModel.OnDeleteCallBack() {
                        @Override
                        public void onSuccess() {
                            new AlertDialog.Builder(requireContext())
                                    .setMessage("Borrado").setPositiveButton(android.R.string.ok, null)
                                    .show();

                            mViewModel.deleteItem(item, new EditorViewModel.OnDeleteCallBack() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
                })
                .setNegativeButton(android.R.string.cancel,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void initData() {

        switch (CURRENT_EDITOR) {
            default: break;
            case ITEM_EDITOR: {
                mViewModel.getItems().observe(getViewLifecycleOwner(), items -> {



                    binding.fragmentEditorRefresh.setRefreshing(false);
                    adapter.clear();
                    adapter.addAll(items);
                });
                break;
            }
            case CATEGORY_EDITOR: {
                mViewModel.getCategories().observe(getViewLifecycleOwner(), items -> {

                    binding.fragmentEditorRefresh.setRefreshing(false);
                    adapter.clear();
                    adapter.addAll(items);
                });
                break;
            }
            case USER_EDITOR: {
                mViewModel.getUsers().observe(getViewLifecycleOwner(), items -> {

                    binding.fragmentEditorRefresh.setRefreshing(false);
                    adapter.clear();
                    adapter.addAll(items);
                });
                break;
            }
            case PROMO_EDITOR: {
                mViewModel.getPromos().observe(getViewLifecycleOwner(), items -> {
                    if (binding.fragmentEditorRefresh.isRefreshing()) {
                        binding.fragmentEditorRefresh.setRefreshing(false);
                    }
                    adapter.clear();
                    adapter.addAll(items);
                });
            }
        }

        onDataChanged();
    }


    public void onDataChanged() {
        switch (CURRENT_EDITOR) {
            case ITEM_EDITOR: mViewModel.retrieveItems(); return;
            case CATEGORY_EDITOR: mViewModel.retrieveCategories(); return;
            case USER_EDITOR:mViewModel.retrieveUsers(); return;
            case PROMO_EDITOR: mViewModel.retrievePromos(); return;
            default:
        }
    }
}