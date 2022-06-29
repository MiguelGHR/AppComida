package com.eme22.applicacioncomida.ui.admin;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.databinding.ActivityAdminBinding;
import com.eme22.applicacioncomida.ui.editor_item.EditorItemFragment;
import com.eme22.applicacioncomida.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.activityAdminToolbar);

        binding.activityAdminToolbar.setNavigationOnClickListener(v -> onBackPressed());

        actionButton = binding.fragmentAdminAdd;

        AcitivityAdminStateAdapter pageAdapter = new AcitivityAdminStateAdapter(this);
        binding.viewPager2.setAdapter(pageAdapter);
        binding.viewPager2.setOffscreenPageLimit(4);
        TabLayoutMediator tablm = new TabLayoutMediator(
                binding.activityAdminTablayout,
                binding.viewPager2,
                (tab, position) -> tab.setText(pageAdapter.getTitle(position))
        );
        tablm.attach();

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.fragmentAdminAdd.setOnClickListener(v -> addItem(position));
            }
        });

        binding.activityAdminToolbar.setNavigationOnClickListener(v -> onBackPressed());




        final Drawable upArrow = ContextCompat.getDrawable( this,com.eme22.floatingsearchview.R.drawable.ic_arrow_back_black_24dp);
        if (upArrow != null) {
            upArrow.setColorFilter(Util.getColor(com.google.android.material.R.attr.colorOnPrimary, this), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }


    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.activity_admin_main_fragment);
        if((f instanceof EditorItemFragment) && ((EditorItemFragment) f).isCanceled()) {
            ((EditorItemFragment) f).onCancel();
        }
        else {
            unFixFab();
            super.onBackPressed();
        }


    }

    public FloatingActionButton getActionButton() {
        return actionButton;
    }

    public void fixFab() {
        binding.fragmentAdminAdd.setCompatElevation(0);
    }

    private void unFixFab() {
        binding.fragmentAdminAdd.setCompatElevation(1);
    }

    private void addItem(int position) {
        fixFab();
        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.add(R.id.activity_admin_main_fragment, EditorItemFragment.newInstance(position), "EditorItemFragment");
        fTransaction.commit();

    }


}