package com.medical.product.helpingFile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dev03 on 06/01/2016.
 */
public class ConnectDetector {




    private Context _context;

    public ConnectDetector(Context co){
        this._context = co;
    }

    public boolean isConnectToInternet(){
        ConnectivityManager connet = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connet != null)
        {
            NetworkInfo[] inf = connet.getAllNetworkInfo();
            if (inf != null)
                for (NetworkInfo networkInfo : inf)
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}



