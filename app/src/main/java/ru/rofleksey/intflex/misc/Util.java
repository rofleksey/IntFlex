package ru.rofleksey.intflex.misc;

import android.content.Context;

public class Util {
    public static int dpToPixels(float dps, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }
}
