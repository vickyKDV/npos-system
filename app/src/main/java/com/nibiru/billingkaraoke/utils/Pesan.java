package com.nibiru.billingkaraoke.utils;

import android.content.Context;


import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nibiru.billingkaraoke.R;
public class Pesan {
    public static NiftyDialogBuilder dialogBuilder;
    public static KProgressHUD hud;
    public final static void showpesan(Context ctx,String judul, String konten, int resid,String btn1,String btn2) {
        dialogBuilder = NiftyDialogBuilder.getInstance(ctx);
        dialogBuilder
                .withTitle(judul)
                .withMessage(konten)
                .withDialogColor(resid)
                .withIcon(R.drawable.kasir_logo)
                .withDuration(700)
                .withEffect(Effectstype.SlideBottom)
                .withButton1Text(btn1)
                .withButton2Text(btn2)
                .isCancelableOnTouchOutside(false)
                .show();
    }


    public final static void loading(Context ctx,String judul,String pesan) {
        hud = KProgressHUD.create(ctx)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(judul)
                .setDetailsLabel(pesan)
                .setDimAmount(0.5f)
                .show();
    }

}
