package com.lmh.classsocial.Login;

import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lmh.classsocial.MainActivity;
import com.lmh.classsocial.MainTask;
import com.lmh.classsocial.R;
import com.lmh.classsocial.SignUp.SignUp;
import com.lmh.classsocial.Static.VarStatic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import es.dmoral.toasty.Toasty;


public class Login extends AppCompatActivity {
    //declare vars
    String userName,userPassword;
    //declare ui vars
    EditText name,password;
    private ProgressBar progressbar;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor shareEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //sharepref
        sharedPreferences=this.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        shareEditor=sharedPreferences.edit();


        //initialize ui vars
        name=(EditText)findViewById(R.id.edt_login_user_name);
        password=(EditText)findViewById(R.id.edt_login_password);
        progressbar=(ProgressBar)findViewById(R.id.progressBar);
    }

    public void onLogin(View view) {
        userName=name.getText().toString();
        userPassword=password.getText().toString();
        LoginAccount();

    }

    public void onLoginClear(View view) {
        clearForm();
    }

    private void clearForm() {
        name.setText("");
        password.setText("");
    }
    private void LoginAccount() {
        progressbar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strreq = new StringRequest(Request.Method.GET,
                VarStatic.getHostName()+"login?"+"user_name="
                        +URLEncoder.encode(userName)+ "&user_password=" +URLEncoder.encode(userPassword),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        // get response
                        Log.d("RESPONSE ",Response);
                        if(!Response.equals("false")){
                            Toasty.success(getApplicationContext(), "Success!", Toast.LENGTH_SHORT, true).show();
                            //store in sharepref
                            Log.d("response",Response);
                            try {
                                JSONArray jsonArray=new JSONArray(Response);
                                JSONObject jsonObject=(JSONObject)jsonArray.get(0);
                                shareEditor.putString("USERID",jsonObject.getString("id"));
                                shareEditor.putString("USERName",jsonObject.getString("name"));
                                shareEditor.commit();
                                Intent intent=new Intent(Login.this,MainTask.class);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSON parse","json err");
                            }


                        }else{
                            Toasty.error(getApplicationContext(), "Error Logging in", Toast.LENGTH_SHORT, true).show();

                        }
                       progressbar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toasty.error(getApplicationContext(), "Error Logging in", Toast.LENGTH_SHORT, true).show();
                progressbar.setVisibility(View.GONE);
            }
        });
        queue.add(strreq);
    }


}
