package com.nibiru.billingkaraoke.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.nibiru.billingkaraoke.Adapter.SubCategoryAdapter;
import com.nibiru.billingkaraoke.Katagori.GridMarginDecoration;
import com.nibiru.billingkaraoke.Model.SubCategoryModel;
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

import static com.nibiru.billingkaraoke.utils.Pesan.hud;


public class SubCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    private Toolbar toolbar;
    public static String kode_subkategori ="";
    public static String nama_subkategori ="";
    private SubCategoryAdapter mAdapter;
    private ArrayList<SubCategoryModel> modelList = new ArrayList<>();
    Context ctx;
    String TAG = SubCategoryActivity.class.getSimpleName();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        // ButterKnife.bind(this);
        findViews();
        initToolbar(getResources().getString(R.string.title_activity_subcategory));
        getDataKategori(CategoryActivity.kode_kategori);
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
        ctx = SubCategoryActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new SubCategoryAdapter(SubCategoryActivity.this, modelList);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(SubCategoryActivity.this, 2);
        recyclerView.addItemDecoration(new GridMarginDecoration(SubCategoryActivity.this, 2, 2, 2, 2));
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
                ArrayList<SubCategoryModel> filterList = new ArrayList<SubCategoryModel>();
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


    private void getDataKategori(String category_cid) {
        Pesan.loading(ctx,"Koneksi ke server","Mohon menunggu...");
        AndroidNetworking.get(Server.akses_api)
                .addQueryParameter("ambil", "subkategori")
                .addQueryParameter("category_cid", category_cid)
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
                                        final SubCategoryModel model = new SubCategoryModel(
                                                data.getString("cid"),
                                                data.getString("sub_name"),
                                                data.getString("sub_image")
                                        );
                                        modelList.add(model);
                                        hud.dismiss();
                                    }
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.SetOnItemClickListener(new SubCategoryAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position, SubCategoryModel model) {
                                            //handle item click events here
                                            kode_subkategori = model.getKodekategori();
                                            nama_subkategori = model.getNamakategori();
                                            PlaySound.playsound(ctx,R.raw.click);
                                            startActivity(new Intent(ctx,MenuActivity.class));
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
