package com.etiennelawlor.discreteslider.library.utilities;

import android.content.res.Resources;

/**
 * Created by etiennelawlor on 12/19/15.
 */
public class DisplayUtility {

    public static int dp2px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
