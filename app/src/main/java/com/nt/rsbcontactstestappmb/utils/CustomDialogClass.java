package com.nt.rsbcontactstestappmb.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.nt.rsbcontactstestappmb.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Context c;
    String number;
    public Dialog d;
    ImageView iv_call, iv_msg;
    public Button cancle;

    public CustomDialogClass(Context a, String number) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.number = number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        cancle = (Button) findViewById(R.id.btn_cancle);
        iv_call = (ImageView) findViewById(R.id.iv_call);
        iv_msg = (ImageView) findViewById(R.id.iv_msg);
        iv_msg.setOnClickListener(this);
        iv_call.setOnClickListener(this);
        cancle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));//change the number
                c.startActivity(callIntent);
                break;
            case R.id.iv_msg:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", number);
                c.startActivity(smsIntent);
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
        dismiss();
    }
}