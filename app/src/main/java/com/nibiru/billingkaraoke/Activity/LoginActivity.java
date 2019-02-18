package com.nibiru.billingkaraoke.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Pesan;
import com.nibiru.billingkaraoke.utils.PlaySound;
import com.nibiru.billingkaraoke.utils.Server;
import com.pixplicity.easyprefs.library.Prefs;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.json.JSONException;
import org.json.JSONObject;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;


public class LoginActivity extends AppCompatActivity {

    //pref
    public static final boolean pref_statuslogin = false;
    public static final String pref_id = "pref_id";
    public static final String pref_fullname = "pref_fullname";
    public static final String pref_username = "pref_username";
    public static final String pref_email = "pref_email";
    int success;
    String TAG = LoginActivity.class.getSimpleName();
    public static boolean status_login;
    public static String id;
    public static String fullname;
    public static String username;
    public static String email;

    Button btn_login;
    MaterialEditText edt_nama, edt_pass;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        load();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PlaySound.playsound(LoginActivity.this,R.raw.click);
                String nama = edt_nama.getText().toString();
                String password = edt_pass.getText().toString();
                if (nama.trim().length() > 0 && password.trim().length() > 0) {
                    login(nama, password);
                } else {
                    Toasty.error(mcontext, "Nama dan Password tidak boleh kosong !!",Toast.LENGTH_SHORT,true).show();

                }
            }
        });

    }





    private void load() {
        AndroidNetworking.initialize(getApplicationContext());
        mcontext = this;
        edt_nama = findViewById(R.id.edtnama);
        edt_pass = findViewById(R.id.edtpassword);
        btn_login = findViewById(R.id.btn_login);
        checkpref();
    }



    private void login(final String nusername,final String npassword) {
        Pesan.loading(mcontext,"Proses Login","Mohon Menunggu...");
        AndroidNetworking.get(Server.akses_api)
                .addQueryParameter("ambil", "login")
                .addQueryParameter("username", nusername)
                .addQueryParameter("password", npassword)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            success = response.getInt("success");
                            if (success==1) {
                                Pesan.hud.dismiss();
                                id          = response.getString("id");
                                username    = response.getString("username");
                                fullname    = response.getString("fullname");
                                email       = response.getString("email");
                                savedPref(true,id,username,fullname,email);
                                Log.d(TAG, "onSuccess: Success" + response.toString());
                                checkpref();
                            }else{
                                Log.d(TAG, "onError: Failed" + response.toString());
                                Toasty.info(LoginActivity.this,response.getString("pesan"),Toast.LENGTH_SHORT,true).show();
                                Pesan.hud.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onError: Failed" + response.toString());
                            Toasty.info(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT,true).show();
                            Pesan.hud.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.info(LoginActivity.this,anError.getMessage(),Toast.LENGTH_SHORT,true).show();
                        Pesan.hud.dismiss();
                    }
                });

    }


    public void savedPref(boolean loginstate,String nid,String nusername,String nfullname,String nemail){
        Prefs.putBoolean(String.valueOf(pref_statuslogin),loginstate);
        Prefs.putString(pref_id,nid);
        Prefs.putString(pref_username,nusername);
        Prefs.putString(pref_fullname,nfullname);
        Prefs.putString(pref_email,nemail);
    }

    private void checkpref(){
        status_login = Prefs.getBoolean(String.valueOf(pref_statuslogin),false);
        if (status_login){
            id = Prefs.getString(pref_id,"");
            username = Prefs.getString(pref_username,"");
            fullname = Prefs.getString(pref_fullname,"");
            email = Prefs.getString(pref_email,"");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Bungee.inAndOut(mcontext);
            finish();
        }
    }
}

