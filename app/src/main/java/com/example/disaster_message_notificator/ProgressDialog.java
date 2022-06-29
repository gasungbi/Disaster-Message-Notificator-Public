package com.example.disaster_message_notificator;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        setCancelable(false);

        ImageView ivGifLoading = (ImageView) findViewById(R.id.ivGifLoading);
        Glide.with((MainActivity)MainActivity.mContext).load(R.drawable.load).into(ivGifLoading);
    }
}
