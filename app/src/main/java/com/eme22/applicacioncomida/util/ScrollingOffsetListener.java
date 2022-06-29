package com.eme22.applicacioncomida.util;

import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;

public class ScrollingOffsetListener implements AppBarLayout.OnOffsetChangedListener {

    private int originalHeight = 0;
    private boolean firstOffset = true;
    private final NestedScrollView nestedScrollView;

    public ScrollingOffsetListener(NestedScrollView nestedScrollView) {
        this.nestedScrollView = nestedScrollView;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(firstOffset) {
            firstOffset = false;
            originalHeight = nestedScrollView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = nestedScrollView.getLayoutParams();
        params.height = originalHeight + (verticalOffset * -1);

        nestedScrollView.setLayoutParams(params);
    }
}
