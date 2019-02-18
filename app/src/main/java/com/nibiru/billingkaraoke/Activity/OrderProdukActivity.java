package com.nibiru.billingkaraoke.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.nibiru.billingkaraoke.utils.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import es.dmoral.toasty.Toasty;
import me.grantland.widget.AutofitTextView;
import spencerstudios.com.bungeelib.Bungee;

import static com.nibiru.billingkaraoke.utils.Pesan.hud;

public class OrderProdukActivity extends AppCompatActivity implements NumberKeyboardListener {

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
        if (nilai > 0 ){
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
        if (nilai > 0 ){
            inputOrder(MenuActivity.kodetrx,MenuActivity.product_cid,MenuActivity.product_name,CategoryActivity.nama_kategori,SubCategoryActivity.nama_subkategori, String.valueOf(nilai),MenuActivity.harga,MenuActivity.diskon,MenuActivity.harga_net,LoginActivity.username);
        }else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.shrink(OrderProdukActivity.this);
        finish();
    }

    private void inputOrder(String trx_cid,String product_cid,String product_name,String category,String subcategory,String qty,String harga,String diskon,String harga_net,String user){
        Pesan.loading(OrderProdukActivity.this,"Input Order","Proses input");
        AndroidNetworking.post(Server.akses_api)
                .addQueryParameter("ambil", "inputtrx")
                .addBodyParameter("trx_cid",trx_cid)
                .addBodyParameter("product_cid",product_cid)
                .addBodyParameter("product_name",product_name)
                .addBodyParameter("category",category)
                .addBodyParameter("subcategory",subcategory)
                .addBodyParameter("qty",qty)
                .addBodyParameter("harga",harga)
                .addBodyParameter("diskon",diskon)
                .addBodyParameter("harga_net",harga_net)
                .addBodyParameter("user",user)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hud.dismiss();
                            success = response.getInt("success");
                            if (success==1){
                                Toasty.info(getApplicationContext(),response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
                                finish();
                            }else{
                                Toasty.error(getApplicationContext(),response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
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
