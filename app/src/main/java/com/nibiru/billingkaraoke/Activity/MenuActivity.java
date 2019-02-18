package com.nibiru.billingkaraoke.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.nibiru.billingkaraoke.Adapter.MenuAdapter;
import com.nibiru.billingkaraoke.Model.MenuModel;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Pesan;
import com.nibiru.billingkaraoke.utils.PlaySound;
import com.nibiru.billingkaraoke.utils.Server;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.support.v4.view.MenuItemCompat;
import android.app.SearchManager;
import android.widget.EditText;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

import static com.nibiru.billingkaraoke.utils.Pesan.dialogBuilder;
import static com.nibiru.billingkaraoke.utils.Pesan.hud;


public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    private Toolbar toolbar;

    private MenuAdapter mAdapter;
    private ArrayList<MenuModel> modelList = new ArrayList<>();
    private Context ctx;
    private String TAG = CategoryActivity.class.getSimpleName();
    int success;
    private LinearLayout lninfoerror;
    private Button btntutup;
    public static String product_cid ="";
    public static String product_name ="";
    public static String harga ="";
    public static String diskon ="";
    public static String harga_net ="";

    private ImageView imgOrderan;
    public static String kodetrx="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // ButterKnife.bind(this);
        findViews();
        initToolbar(CategoryActivity.nama_kategori);
        getDataProduk(SubCategoryActivity.kode_subkategori);



    }

    private void findViews() {
        ctx = MenuActivity.this;
        imgOrderan = findViewById(R.id.img_orderan);
        lninfoerror = findViewById(R.id.ln_infoerror);
        btntutup = findViewById(R.id.btn_tutup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MenuAdapter(MenuActivity.this, modelList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btntutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgOrderan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx,DetailOrderActivity.class));
                Bungee.shrink(ctx);
            }
        });
    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }


    private void getDataProduk(String sub_cid){
        Pesan.loading(ctx,"Koneksi ke server","Mohon menunggu...");
        AndroidNetworking.get(Server.akses_api)
                .addQueryParameter("ambil", "product")
                .addQueryParameter("sub_cid", sub_cid)
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
                                        final MenuModel model = new MenuModel(
                                                data.getString("cid"),
                                                data.getString("product_name"),
                                                data.getString("product_image"),
                                                data.getString("product_harga"),
                                                data.getString("product_diskon"),
                                                data.getString("product_stock"),
                                                data.getString("harga_net")
                                        );
                                        modelList.add(model);
                                        hud.dismiss();
                                    }

                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.SetOnItemClickListener(new MenuAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position, MenuModel model) {
                                            PlaySound.playsound(ctx,R.raw.click);
                                            product_cid = model.getCid();
                                            product_name = model.getProduct_name();
                                            harga = model.getProduct_harga();
                                            diskon = model.getProduct_diskon();
                                            harga_net = model.getHarga_net();
                                            cek_kodetrx();
//                                            startActivity(new Intent(ctx,OrderProdukActivity.class));
//                                            Bungee.shrink(ctx);


                                        }
                                    });
                                } else {
                                    hud.dismiss();
//                                    Toasty.info(ctx, Pesan, Toast.LENGTH_SHORT, true).show();
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

//    private void hapustrx(String trx_cid){
//        Pesan.loading(MenuActivity.this,"Delete Order","");
//        AndroidNetworking.post(Server.akses_api)
//                .addQueryParameter("ambil", "deletetrx")
//                .addBodyParameter("trx_cid",trx_cid)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            success = response.getInt("success");
//                            if (success==1){
//                                Toasty.info(getApplicationContext(),response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
//                                hud.dismiss();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toasty.error(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT,true).show();
//                            hud.dismiss();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Toasty.error(getApplicationContext(),anError.getMessage(),Toast.LENGTH_SHORT,true).show();
//                        hud.dismiss();
//                    }
//                });
//    }

    private void cek_kodetrx(){
        Pesan.loading(MenuActivity.this,"Cek Last Order","");
        AndroidNetworking.get(Server.akses_api)
                .addQueryParameter("ambil", "kodetrx")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            success = response.getInt("success");
                            if (success==1) {
                                hud.dismiss();
//                                Pesan.showpesan(ctx, "Konfirmasi", response.getString("pesan") + ", hapus pesanan ?", getResources().getColor(R.color.merah), "Batal", "Hapus");
                                kodetrx = response.getString("kode");
                                startActivity(new Intent(ctx,OrderProdukActivity.class));
                                Bungee.shrink(ctx);
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.zoom(ctx);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.menu_search, menu);
            // Retrieve the SearchView and plug it into SearchManager
            final SearchView searchView = (SearchView) MenuItemCompat
                    .getActionView(menu.findItem(R.id.action_search));
            SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            //changing edittext color
            EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
            searchEdit.setTextColor(Color.WHITE);
            searchEdit.setHintTextColor(Color.WHITE);
            searchEdit.setBackgroundColor(Color.TRANSPARENT);
            searchEdit.setHint("Cari " + CategoryActivity.nama_kategori);
            InputFilter[] fArray = new InputFilter[2];
            fArray[0] = new InputFilter.LengthFilter(40);
            fArray[1] = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {

                        if (!Character.isLetterOrDigit(source.charAt(i)))
                            return "";
                    }
                    return null;
                }
            };
            searchEdit.setFilters(fArray);
            View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            v.setBackgroundColor(Color.TRANSPARENT);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    ArrayList<MenuModel> filterList = new ArrayList<MenuModel>();
                    if (s.length() > 0) {
                        for (int i = 0; i < modelList.size(); i++) {
                            if (modelList.get(i).getProduct_name().toLowerCase().contains(s.toString().toLowerCase())) {
                                filterList.add(modelList.get(i));
                                mAdapter.updateList(filterList);
                            }
                        }

                    } else {
                        mAdapter.updateList(modelList);
                    }
                    return false;
                }
            });
        return true;
    }



}
