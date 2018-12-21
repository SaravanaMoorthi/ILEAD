package com.tvscs.ilead.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ilead.tvscs.com.ilead.R;

public class LoaderDialog {

    Context mContext;
    Dialog dialog;
    SweetAlertDialog sweetAlertDialog;

    public LoaderDialog(Context context) {
        this.mContext = context;
    }

    public void showDialog() {

        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.loader_dialog);
        dialog.setTitle("Loading, Please Wait...");
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide
        Glide.with(mContext)
                .load(R.mipmap.loader)
                .placeholder(R.mipmap.loader)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(100, 100)
                .dontAnimate()
                .centerCrop() // scale to fill the ImageView and crop any extra
                .into(imageViewTarget);
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog() {
        dialog.dismiss();
    }

    public void showProgressDialog() {
        try {
            sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(R.color.colorPrimaryDark);
            sweetAlertDialog.getProgressHelper().setRimColor(R.color.colorOrange);
            sweetAlertDialog.setTitleText("Loading");
            sweetAlertDialog.setCancelable(true);
            sweetAlertDialog.show();
        } catch (NullPointerException n) {
            n.printStackTrace();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        try {
            sweetAlertDialog.dismiss();
        } catch (NullPointerException n) {
            n.printStackTrace();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

}
