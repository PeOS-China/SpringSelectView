package com.peos.springselect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/3/31.
 */

public class NetworkUtil {

    public static boolean isHasNetwork(Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isAvailable()){   //判断网络连接是否打开
            return  mNetworkInfo.isConnected();
        }

        return false;
    }
}
