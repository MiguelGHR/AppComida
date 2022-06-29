package com.eme22.applicacioncomida.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.data.model.SearchResult;
import com.eme22.applicacioncomida.data.model.User;
import com.eme22.applicacioncomida.databinding.ActivityMainBinding;
import com.eme22.applicacioncomida.ui.editor_item.EditorItemFragment;
import com.eme22.applicacioncomida.ui.login.LoginActivity;
import com.eme22.applicacioncomida.ui.user.UserFragment;
import com.eme22.floatingsearchview.FloatingSearchView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private User user;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = (User) getIntent().getSerializableExtra(LoginActivity.EXTRA_USER);

        ViewPager2 viewPager = binding.viewPager2;
        BottomNavigationView tabLayout = binding.navigation;
        Toolbar toolbar = binding.toolbar;

        setSupportActionBar(toolbar);

        MainStateAdapter pageAdapter = new MainStateAdapter(this);
        viewPager.setAdapter(pageAdapter);
        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(pageAdapter.getItemCount() - 1);

        BottomNavItemSelectedListener listener = new BottomNavItemSelectedListener(viewPager, toolbar);
        tabLayout.setOnItemSelectedListener(listener);

        /*
        TabLayoutMediator tablm = new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> tab.setText(pageAdapter.getTitle(position))
        );
        tablm.attach();

        for (int i = 0; i < tabLayout.getTabCount() ; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pageAdapter.getTabView(i));
        }
        */

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        openUserFragment();
        return true;
    }

    private void openUserFragment() {
        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.addToBackStack(null);
        fTransaction.add(R.id.main_fragment, UserFragment.newInstance(), "UserFragment");
        fTransaction.commit();
    }

    public User getUser() {
        return user;
    }

    public void buySuccess() {
        onBackPressed();
        onBackPressed();

        binding.viewPager2.setCurrentItem(2);
        /*
        TabLayout.Tab tab = binding.tabLayout.getTabAt(2);
        if (tab != null) {
            tab.select();
        }
        */
    }

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        if((f instanceof EditorItemFragment) && ((EditorItemFragment) f).isCanceled()) {
            ((EditorItemFragment) f).onCancel();
            return;
        }

        if (f == null) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Por favor presione de nuevo para salir", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
            return;
        }



        super.onBackPressed();



    }
}