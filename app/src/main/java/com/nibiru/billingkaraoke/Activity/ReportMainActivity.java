package com.nibiru.billingkaraoke.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import android.support.v4.widget.SwipeRefreshLayout;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.nibiru.billingkaraoke.Adapter.ReportAdapter;
import com.nibiru.billingkaraoke.Model.ReportModel;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Pesan;
import com.nibiru.billingkaraoke.utils.Server;
import android.widget.Toast;
import android.os.Handler;
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
public class ReportMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshRecyclerList;
    private ReportAdapter mAdapter;
    private Context ctx;
    private String TAG = CategoryActivity.class.getSimpleName();
    int success;
    private ArrayList<ReportModel> modelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_main);

        // ButterKnife.bind(this);
        findViews();
        initToolbar("Report");
        swipeRefreshRecyclerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do your stuff on refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (swipeRefreshRecyclerList.isRefreshing())
                            swipeRefreshRecyclerList.setRefreshing(false);
                    }
                }, 5000);

            }
        });

        getdatapenjualan();
    }

    private void findViews() {
        ctx = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshRecyclerList = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_recycler_list);
        mAdapter = new ReportAdapter(ReportMainActivity.this, modelList, "Header", "Footer");
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ReportMainActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        searchEdit.setHint("Search");

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
                ArrayList<ReportModel> filterList = new ArrayList<ReportModel>();
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

    private void getdatapenjualan() {
        Pesan.loading(ctx,"Koneksi ke server","Mohon menunggu...");
        AndroidNetworking.get(Server.akses_api)
                .addQueryParameter("ambil", "reportpenjualan")
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
                                        final ReportModel model = new ReportModel(
                                                data.getString("product_image"),
                                                data.getString("product_name"),
                                                data.getString("harga_net"),
                                                data.getString("qty"),
                                                data.getString("total_harga")
                                        );
                                        modelList.add(model);
                                        hud.dismiss();
                                    }
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.SetOnItemClickListener(new ReportAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position, ReportModel model) {
                                            //handle item click events here
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



//    private void setAdapter() {
//
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.SetOnItemClickListener(new ReportAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, ReportModel model) {
//                //handle item click events here
//            }
//        });
//        mAdapter.SetOnHeaderClickListener(new ReportAdapter.OnHeaderClickListener() {
//            @Override
//            public void onHeaderClick(View view, String headerTitle) {
//                //handle item click events here
//            }
//        });
//        mAdapter.SetOnFooterClickListener(new ReportAdapter.OnFooterClickListener() {
//            @Override
//            public void onFooterClick(View view, String footerTitle) {
//                //handle item click events here
//
//            }
//        });
//
//
//    }


}
