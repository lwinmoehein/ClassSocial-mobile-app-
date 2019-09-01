package com.lmh.classsocial;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lmh.classsocial.SignUp.SignUp;
import com.lmh.classsocial.Static.FunctionsStatic;
import com.lmh.classsocial.Static.ImageStatic;
import com.lmh.classsocial.Static.UIStatic;
import com.lmh.classsocial.Static.VarStatic;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class CreatePost extends AppCompatActivity {
    String imageurl = "";
    String postText;
    String userId, userName;

    EditText edtPost;
    ImageView imgPost;

    Context context;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                imageurl = getPath(selectedImage);
                if (!imageurl.equals(null)) {
                    File file = new File(imageurl);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Picasso.with(getApplicationContext())
                            .load(file)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).
                            placeholder(R.mipmap.background_placeholder).
                            error(R.mipmap.background_placeholder)
                            .into(imgPost);
                }
                String file_extn = imageurl.substring(imageurl.lastIndexOf(".") + 1);

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imageurl = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        context = getApplicationContext();
        userId = FunctionsStatic.getUserId(this);
        userName = FunctionsStatic.getUserName(this);
        //initialize
        edtPost = (EditText) findViewById(R.id.edt_post_text);
        imgPost = (ImageView) findViewById(R.id.img_post);

        //

    }

    public void onCreatePost(View view) {
        postText = edtPost.getText().toString();
        createPost();
    }

    public void onChooseImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    private void createPost() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strreq = new StringRequest(Request.Method.POST,
                VarStatic.getHostName()+"post?"+"acc_id="
                        + URLEncoder.encode(userId)+ "&acc_name=" +URLEncoder.encode(userName)+
                        "&post_body="+URLEncoder.encode(postText),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        // get response
                        if(Response.equals("success")){
                            Toasty.success(getApplicationContext(), "Posted!", Toast.LENGTH_SHORT, true).show();
                            finish();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toasty.error(getApplicationContext(), "Error posting", Toast.LENGTH_SHORT, true).show();
            }
        });
        queue.add(strreq);
    }

    public void onExitPost(View view) {
        finish();
    }



}
