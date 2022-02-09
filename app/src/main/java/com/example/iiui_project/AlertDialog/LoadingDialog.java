package com.example.iiui_project.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.iiui_project.R;

public class LoadingDialog {
    Activity mContext ;
    AlertDialog alertDialog ;

    public LoadingDialog(Activity mContext) {


        this.mContext = mContext;
    }

    public  void LoadindAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext ,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert) ;
        LayoutInflater inflater = mContext.getLayoutInflater() ;
        alert.setView(inflater.inflate(R.layout.custom_dialog1, null)) ;
        alert.setCancelable(true);
        alertDialog = alert.create();
        alert.show();

    }

    public  void Dismiss(){
//        alertDialog.dismiss();
  //      alertDialog.hide();
    }
}
