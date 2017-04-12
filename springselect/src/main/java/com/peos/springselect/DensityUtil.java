package com.peos.springselect;

import android.content.res.Resources;

/**
 * Created by Administrator on 2017/3/31.
 */

public class DensityUtil {

    private static Resources sRes = Resources.getSystem();
    private static int sDensityDpi = sRes.getDisplayMetrics().densityDpi;

    public static int dp2px(float value) {
        final float scale = sDensityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }
}
