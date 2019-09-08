package com.example.app_video;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PublicMethod {
    public boolean checkConnectInternet(Context context){
        boolean connected= false;
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED)
            connected= true;
        return connected;
    }
}
