package com.nibiru.billingkaraoke.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Pesan;
import com.nibiru.billingkaraoke.utils.PlaySound;
import com.pixplicity.easyprefs.library.Prefs;
import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;
import static com.nibiru.billingkaraoke.utils.Pesan.dialogBuilder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtidkasir,txtlevel;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtnama;
    CircleImageView img_foto;
    Button btn_produk,btn_transaksi,btn_report,btn_logout;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        loaddata();
    }

    private void loaddata() {
        txtidkasir = findViewById(R.id.txtiduser);
        txtnama = findViewById(R.id.txtnmuser);
        txtlevel = findViewById(R.id.txtstatususer);
        img_foto = findViewById(R.id.profile_image);
        btn_produk = findViewById(R.id.btn_produk);
        btn_transaksi = findViewById(R.id.btn_transaksi);
        btn_report = findViewById(R.id.btn_report);
        btn_logout = findViewById(R.id.btn_logout);
        btn_produk.setOnClickListener(this);
        btn_transaksi.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        txtidkasir.setText(LoginActivity.id);
        txtnama.setText(LoginActivity.username);
        txtlevel.setText(LoginActivity.fullname);
//        ImageServiceManager.imageservice(MainActivity.this,LoginActivity.pprofile,R.drawable.kasir_logo,img_foto);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_produk: {
                PlaySound.playsound(this,R.raw.click);
                startActivity(new Intent(ctx,CategoryActivity.class));
                Bungee.zoom(ctx);
                break;
            }
            case R.id.btn_transaksi:{
                PlaySound.playsound(this,R.raw.click);
                startActivity(new Intent(ctx,DetailOrderActivity.class));
                Bungee.zoom(ctx);
                break;
            }
            case R.id.btn_report:{
                PlaySound.playsound(this,R.raw.click);
                startActivity(new Intent(ctx,ReportMainActivity.class));
                Bungee.zoom(ctx);
                break;
            }
            case R.id.btn_logout:{
                PlaySound.playsound(this,R.raw.click);
                Pesan.showpesan(ctx,"Konfirmasi","Anda yakin untuk keluar ?",getResources().getColor(R.color.merah),"Keluar","Batal");
                dialogBuilder.setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlaySound.playsound(ctx, R.raw.click);
                        Prefs.clear();
                        startActivity(new Intent(ctx, LoginActivity.class));
                        Bungee.inAndOut(ctx);
                        finish();
                    }}).setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PlaySound.playsound(ctx, R.raw.click);
                            dialogBuilder.dismiss();
                        }
                        });
                break;
            }
        }
    }
}