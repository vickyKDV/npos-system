package com.nibiru.billingkaraoke.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.nibiru.billingkaraoke.Adapter.DetailOrderAdapter;
import com.nibiru.billingkaraoke.Model.DetailOrderModel;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Pesan;
import com.nibiru.billingkaraoke.utils.PlaySound;
import com.nibiru.billingkaraoke.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

import static com.nibiru.billingkaraoke.utils.Pesan.dialogBuilder;
import static com.nibiru.billingkaraoke.utils.Pesan.hud;


public class DetailOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DetailOrderAdapter mAdapter;
    private ArrayList<DetailOrderModel> modelList = new ArrayList<>();
    Context ctx;
    String TAG = CategoryActivity.class.getSimpleName();
    int success;
    LinearLayout lninfoerror;
    Button btn_tutupDetailOrder,btn_prosesdetailOrder,btn_tutupOrderactivity,btn_hapus;
    public static DetailOrderModel model;
    public static String mproduct_cid;
    public static String mtrx_cid;
    public static String mqty;
    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        findViews();
         vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetailOrder();
    }

    private void findViews() {
        ctx = DetailOrderActivity.this;
        lninfoerror = findViewById(R.id.ln_infoerror);
        btn_tutupDetailOrder = findViewById(R.id.btn_tutupDetailOrder);
        btn_hapus = findViewById(R.id.btn_deletetrx);
        btn_prosesdetailOrder = findViewById(R.id.btn_prosesDetailOrder);
        btn_tutupOrderactivity = findViewById(R.id.btn_tutupOrderactivity);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new DetailOrderAdapter(DetailOrderActivity.this, modelList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btn_tutupDetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Bungee.shrink(ctx);

            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.playsound(ctx,R.raw.click);
                Pesan.showpesan(ctx,"Konfirmasi","Hapus orderan ?",getResources().getColor(R.color.maincolor),"Hapus","Batal");
                dialogBuilder
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            PlaySound.playsound(ctx, R.raw.click);
                            DeleteOrder(model.getTrx_cid());
                            }})
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PlaySound.playsound(ctx, R.raw.click);
                                dialogBuilder.dismiss();
                            }
                        });
            }
        });

        btn_prosesdetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaySound.playsound(ctx,R.raw.click);
                Pesan.showpesan(ctx,"Konfirmasi","Kirim orderan ke kicthen ?",getResources().getColor(R.color.maincolor),"Kirim","Batal");
                dialogBuilder
                        .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlaySound.playsound(ctx, R.raw.click);
                        //PROSES PRINT DISINI
                        ProsesOrderan(model.getTrx_cid());
                    }})
                        .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlaySound.playsound(ctx, R.raw.click);
                        dialogBuilder.dismiss();
                    }
                });
            }
        });

        btn_tutupOrderactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Bungee.shrink(ctx);

            }
        });
    }

    private void DeleteOrder(String trx_cid){
        Pesan.loading(DetailOrderActivity.this,"Delete Item","Proses Delete");
        AndroidNetworking.post(Server.akses_api)
                .addQueryParameter("ambil", "deletetrx")
                .addBodyParameter("trx_cid",trx_cid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            success = response.getInt("success");
                            if (success==1){
                                Toasty.info(getApplicationContext(),response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
                                hud.dismiss();
                                finish();
                            }else{
                                Toasty.error(getApplicationContext(),response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
                                hud.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT,true).show();
                            hud.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(getApplicationContext(),anError.getMessage(),Toast.LENGTH_SHORT,true).show();
                        hud.dismiss();
                    }
                });
    }

    private void ProsesOrderan(String trx_cid){
        Pesan.loading(DetailOrderActivity.this,"Proses penjualan","Proses");
        AndroidNetworking.post(Server.akses_api)
                .addQueryParameter("ambil", "prosesorderan")
                .addBodyParameter("trx_cid",trx_cid)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            success = response.getInt("success");
                            if (success==1){
                                Toasty.info(getApplicationContext(),response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
                                hud.dismiss();
                                finish();
                            }else{
                                Toasty.error(getApplicationContext(),response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
                                hud.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT,true).show();
                            hud.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(getApplicationContext(),anError.getMessage(),Toast.LENGTH_SHORT,true).show();
                        hud.dismiss();
                    }
                });
    }

    private void getDetailOrder(){
        Pesan.loading(ctx,"Koneksi ke server","Mohon menunggu...");
        AndroidNetworking.get(Server.akses_api)
                .addQueryParameter("ambil", "detailorderan")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String s) {
                        Log.d(TAG, "onResponse: " + s);
                        {
                            try {
                                modelList.clear();
                                JSONObject jsonObject = new JSONObject(s);
                                success = jsonObject.getInt("success");
                                String pesan = jsonObject.getString("pesan");
                                JSONArray array = jsonObject.getJSONArray("result");
                                if (success == 1) {
                                    lninfoerror.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject data = array.getJSONObject(i);
                                        //adding the product to product list

                                         model = new DetailOrderModel(
                                                 data.getString("trx_cid"),
                                                 data.getString("product_cid"),
                                                 data.getString("product_name"),
                                                 data.getString("qty"),
                                                 data.getString("diskon"),
                                                 data.getString("harga"),
                                                 data.getString("harga_net"),
                                                 data.getString("total_harga")
                                        );
                                        modelList.add(model);
                                        hud.dismiss();

                                    }
                                    Log.d(TAG, "onResponse: " + s);
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.SetOnItemClickListener(new DetailOrderAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position, DetailOrderModel model) {
//                                            PlaySound.playsound(ctx,R.raw.click);
                                            vibe.vibrate(100);
                                            mtrx_cid = model.getTrx_cid();
                                            mproduct_cid = model.getProduct_cid();
                                            mqty = model.getQty();
                                            startActivity(new Intent(ctx, UpdateOrderActivity.class));
                                            Bungee.shrink(ctx);

                                        }
                                    });
                                } else {
                                    hud.dismiss();
                                    lninfoerror.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                hud.dismiss();
                                e.printStackTrace();
                                Toasty.error(ctx, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                lninfoerror.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                        hud.dismiss();
                        Toasty.error(getApplicationContext(), error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        lninfoerror.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.zoom(ctx);
        finish();
    }


}
