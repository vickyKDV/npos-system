package com.nibiru.billingkaraoke.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.nibiru.billingkaraoke.Adapter.CategoryAdapter;
import com.nibiru.billingkaraoke.Model.CategoryModel;
import com.nibiru.billingkaraoke.Katagori.GridMarginDecoration;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Pesan;
import com.nibiru.billingkaraoke.utils.PlaySound;
import com.nibiru.billingkaraoke.utils.Server;

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

import static com.nibiru.billingkaraoke.utils.Pesan.hud;


public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    private Toolbar toolbar;
    public static String kode_kategori ="";
    public static String nama_kategori ="";
    private CategoryAdapter mAdapter;
    private ArrayList<CategoryModel> modelList = new ArrayList<>();
    Context ctx;
    String TAG = CategoryActivity.class.getSimpleName();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // ButterKnife.bind(this);
        findViews();
        initToolbar(getResources().getString(R.string.title_activity_category));
        getDataKategori();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Bungee.zoom(ctx);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViews() {
        ctx = CategoryActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CategoryAdapter(CategoryActivity.this, modelList);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(CategoryActivity.this, 2);
        recyclerView.addItemDecoration(new GridMarginDecoration(CategoryActivity.this, 2, 2, 2, 2));
        recyclerView.setLayoutManager(layoutManager);
    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
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
        searchEdit.setHint("Cari Kategori");
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
                ArrayList<CategoryModel> filterList = new ArrayList<CategoryModel>();
                if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (modelList.get(i).getNamakategori().toLowerCase().contains(s.toString().toLowerCase())) {
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


    private void getDataKategori() {
        Pesan.loading(ctx,"Koneksi ke server","Mohon menunggu...");
        AndroidNetworking.get(Server.akses_api)
                .addQueryParameter("ambil", "kategori")
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

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject data = array.getJSONObject(i);
                                        //adding the product to product list
                                        final CategoryModel model = new CategoryModel(
                                                data.getString("cid"),
                                                data.getString("category_name"),
                                                data.getString("category_image")
                                        );
                                        modelList.add(model);
                                        hud.dismiss();
                                    }
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.SetOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position, CategoryModel model) {
                                            //handle item click events here
                                            kode_kategori = model.getKodekategori();
                                            nama_kategori = model.getNamakategori();
                                            PlaySound.playsound(ctx,R.raw.click);
                                            startActivity(new Intent(ctx,SubCategoryActivity.class));
                                            Bungee.zoom(ctx);
                                        }
                                    });
                                } else {
                                    hud.dismiss();
                                    Toasty.info(ctx, pesan, Toast.LENGTH_SHORT, true).show();

                                }
                            } catch (JSONException e) {
                                hud.dismiss();
                                e.printStackTrace();
                                Toasty.error(ctx, e.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                        hud.dismiss();
                        Toasty.error(ctx, error.getErrorDetail(), Toast.LENGTH_SHORT).show();
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
