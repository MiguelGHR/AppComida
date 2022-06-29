package com.eme22.applicacioncomida.ui.main;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.eme22.applicacioncomida.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavItemSelectedListener implements BottomNavigationView.OnItemSelectedListener {

  private final Toolbar toolbar;
  private final ViewPager2 viewPager;

  public BottomNavItemSelectedListener(ViewPager2 viewPager, Toolbar toolbar) {
    this.toolbar = toolbar;
    this.viewPager = viewPager;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //toolbar.setTitle(item.getTitle());
    int itemId = item.getItemId();
    if (itemId == R.id.navigation_home) {
      viewPager.setCurrentItem(0);
      return true;
    } else if (itemId == R.id.navigation_categories) {
      viewPager.setCurrentItem(1);
      return true;
    } else if (itemId == R.id.navigation_cart) {
      viewPager.setCurrentItem(2);
      return true;
    }
    return false;
  }
}
