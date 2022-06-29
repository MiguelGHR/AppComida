package com.eme22.applicacioncomida.ui.admin;

import static com.eme22.applicacioncomida.ui.editor.EditorFragment.CATEGORY_EDITOR;
import static com.eme22.applicacioncomida.ui.editor.EditorFragment.ITEM_EDITOR;
import static com.eme22.applicacioncomida.ui.editor.EditorFragment.PROMO_EDITOR;
import static com.eme22.applicacioncomida.ui.editor.EditorFragment.USER_EDITOR;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eme22.applicacioncomida.R;
import com.eme22.applicacioncomida.ui.cart.CartFragment;
import com.eme22.applicacioncomida.ui.category.CategoryFragment;
import com.eme22.applicacioncomida.ui.editor.EditorFragment;
import com.eme22.applicacioncomida.ui.home.HomeFragment;

import java.util.ArrayList;

public class AcitivityAdminStateAdapter extends FragmentStateAdapter {

    private final FragmentActivity fragmentActivity;

    public String getTitle(int position) {
        switch(position) {
            case 0:
                return this.fragmentActivity.getString(R.string.items);
            case 1:
                return this.fragmentActivity.getString(R.string.categories);
            case 2:
                return this.fragmentActivity.getString(R.string.promos);
            case 3:
                return this.fragmentActivity.getString(R.string.users);
            default:
                return null;
        }
    }

    public AcitivityAdminStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return EditorFragment.newInstance(ITEM_EDITOR);
            case 1:
                return EditorFragment.newInstance(CATEGORY_EDITOR);
            case 2:
                return EditorFragment.newInstance(PROMO_EDITOR);
            case 3:
                return EditorFragment.newInstance(USER_EDITOR);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
