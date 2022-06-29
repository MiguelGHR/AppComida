package com.eme22.applicacioncomida.util;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.AttrRes;

public class Util {
    public static int getColor (@AttrRes final int attr, final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme ().resolveAttribute (attr, value, true);
        return value.data;
    }
}
