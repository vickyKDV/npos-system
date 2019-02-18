package com.nibiru.billingkaraoke.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Pesan;
import com.nibiru.billingkaraoke.utils.PlaySound;
import com.nibiru.billingkaraoke.utils.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import es.dmoral.toasty.Toasty;
import me.grantland.widget.AutofitTextView;
import spencerstudios.com.bungeelib.Bungee;

import static com.nibiru.billingkaraoke.utils.Pesan.dialogBuilder;
import static com.nibiru.billingkaraoke.utils.Pesan.hud;

public class UpdateOrderActivity extends AppCompatActivity implements NumberKeyboardListener {

    private static final int MAX_ALLOWED_AMOUNT = 999;
    TextView txtqty;
    AutofitTextView txttitledialog;
    private int amount;
    private int nilai;
    private NumberFormat nf = NumberFormat.getInstance();
    NumberKeyboard numberKeyboard;
    int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_produk);
        this.setFinishOnTouchOutside(true);
        firstload();


    }

    private void firstload(){
        txtqty = findViewById(R.id.amount);
        txttitledialog = findViewById(R.id.txttitledialog);
        numberKeyboard = findViewById(R.id.numberKeyboard);
        numberKeyboard.setListener(this);
        numberKeyboard.setRightAuxButtonBackground(R.drawable.roundbutton_red);
        numberKeyboard.setRightAuxButtonIcon(R.drawable.ic_clear_white_48dp);
        txttitledialog.setText("Masukan Jumlah "+MenuActivity.product_name);
        txtqty.setText(DetailOrderActivity.mqty);

        Toast.makeText(UpdateOrderActivity.this,DetailOrderActivity.mtrx_cid+" "+DetailOrderActivity.mproduct_cid,Toast.LENGTH_SHORT).show();;
    }

    @Override
    public void onNumberClicked(int number) {
        int newAmount = (int) (amount * 10.0 + number);
        if (newAmount <= MAX_ALLOWED_AMOUNT) {
            amount = newAmount;
            showAmount();
        }
    }

    private void showAmount() {
        txtqty.setText(nf.format(amount));
        nilai = Integer.parseInt(txtqty.getText().toString());
        if (nilai != Integer.parseInt(DetailOrderActivity.mqty)){
            numberKeyboard.setRightAuxButtonBackground(R.drawable.roundbutton);
            numberKeyboard.setRightAuxButtonIcon(R.drawable.ic_done_black_24dp);
        }else{
            numberKeyboard.setRightAuxButtonBackground(R.drawable.roundbutton_red);
            numberKeyboard.setRightAuxButtonIcon(R.drawable.ic_clear_white_48dp);
        }
    }

    @Override
    public void onLeftAuxButtonClicked() {
        amount = (int) (amount / 10.0);
        showAmount();
    }

    @Override
    public void onRightAuxButtonClicked() {
        nilai = Integer.parseInt(txtqty.getText().toString());
        if ( nilai != Integer.parseInt(DetailOrderActivity.mqty) && nilai != 0){
            UpdateQtyOrder(DetailOrderActivity.mtrx_cid,DetailOrderActivity.mproduct_cid,txtqty.getText().toString());
        }else {
            if (nilai == 0) {
                Pesan.showpesan(UpdateOrderActivity.this, "Hapus Orderan", "Qty Order '0' akan di hapus ?", getResources().getColor(R.color.merah),"Hapus","Batal");
                dialogBuilder.setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlaySound.playsound(UpdateOrderActivity.this, R.raw.click);
                        DeleteItem(DetailOrderActivity.mtrx_cid, DetailOrderActivity.mproduct_cid);
                    }
                }).setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlaySound.playsound(UpdateOrderActivity.this, R.raw.click);
                        dialogBuilder.dismiss();
                    }
                });
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.shrink(UpdateOrderActivity.this);
        finish();
    }

    private void UpdateQtyOrder(String trx_cid,String product_cid,String qty){
        Pesan.loading(UpdateOrderActivity.this,"Input Order","Proses input");
        AndroidNetworking.post(Server.akses_api)
                .addQueryParameter("ambil","updateqty")
                .addBodyParameter("trx_cid",trx_cid)
                .addBodyParameter("product_cid",product_cid)
                .addBodyParameter("qty",qty)
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

    private void DeleteItem(String trx_cid,String product_cid){
        Pesan.loading(UpdateOrderActivity.this,"Delete Item","Proses Delete");
        AndroidNetworking.post(Server.akses_api)
                .addQueryParameter("ambil", "deleteitem")
                .addBodyParameter("trx_cid", trx_cid)
                .addBodyParameter("product_cid", product_cid)
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


}
