package com.aindri.hemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.Html;

public class MyReceiver  extends BroadcastReceiver {
    Context con;
    AlertDialog.Builder dialog1;
    Activity a;

    public MyReceiver(Context con,  Activity a) {
        this.con = con;
        this.a = a;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        dialog1=new AlertDialog.Builder(con);
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            final boolean noConnectivity=intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);


            dialog1.setTitle("Oops!");
            dialog1.setIcon(R.drawable.error);
            dialog1.setMessage(Html.fromHtml("<font color='#FFFFF'>No Internet Access </font>"));
            dialog1.setCancelable(false);
            dialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(noConnectivity){
                        dialog1.show();
                    }
                    else {
                        dialog.dismiss();
                    }

                }
            });
            if(noConnectivity){
                dialog1.show();
            }
        }
    }
}
