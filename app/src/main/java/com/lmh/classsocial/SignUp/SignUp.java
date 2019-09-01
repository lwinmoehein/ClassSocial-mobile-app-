package com.lmh.classsocial.SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lmh.classsocial.MainActivity;
import com.lmh.classsocial.R;
import com.lmh.classsocial.Static.VarStatic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import es.dmoral.toasty.Toasty;


public class SignUp extends AppCompatActivity {
    //initialize  vars
    String accName, accPassword, accConfirmPassword, accPhoneNo;
    //initialize ui vars
    EditText Name, Password, ConfirmPassword, PhoneNo;

    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //

        Name = (EditText) findViewById(R.id.edt_acc_name);
        Password = (EditText) findViewById(R.id.edt_pass);
        ConfirmPassword = (EditText) findViewById(R.id.edt_pass_confirm);
        PhoneNo = (EditText) findViewById(R.id.edt_phone_no);
        progressbar=(ProgressBar)findViewById(R.id.progressBar);



    }

    public void onClear(View view) {
        clearTextViews();
    }

    private void clearTextViews() {
        Name.setText("");
        Password.setText("");
        ConfirmPassword.setText("");
        PhoneNo.setText("");
    }

    public void onCreateAccount(View view) {
        accName = Name.getText().toString();
        accPassword = Password.getText().toString();
        accConfirmPassword = ConfirmPassword.getText().toString();
        accPhoneNo = PhoneNo.getText().toString();
        if (accPassword.equals(accConfirmPassword)) {
             createAccount();
        } else {
           Toasty.warning(getApplicationContext(),"Confirm password correctly").show();
        }

    }

    private void createAccount() {
        progressbar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strreq = new StringRequest(Request.Method.POST,
                VarStatic.getHostName()+"student?"+"student_name="
                        +URLEncoder.encode(accName)+ "&student_phoneno=" +URLEncoder.encode(accPhoneNo)+
                "&student_password="+URLEncoder.encode(accPassword)+"&" +
                        "student_email=not given"+"&is_followed=no&is_ec=no",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        // get response
                        if(Response.equals("success")){
                            Toasty.success(getApplicationContext(), "Success!", Toast.LENGTH_SHORT, true).show();
                            Intent intent=new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);

                        }
                        progressbar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toasty.error(getApplicationContext(), "Error creating account", Toast.LENGTH_SHORT, true).show();
                progressbar.setVisibility(View.GONE);
            }
        });
        queue.add(strreq);
    }
    //Sign up Network class


}
