package com.lmh.classsocial.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lmh.classsocial.MainActivity;
import com.lmh.classsocial.Model.Post;
import com.lmh.classsocial.R;
import com.lmh.classsocial.SignUp.SignUp;
import com.lmh.classsocial.Static.FunctionsStatic;
import com.lmh.classsocial.Static.VarStatic;
import com.lmh.classsocial.UIAdapters.PostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * Created by E on 6/13/2018.
 */

public class PostFragment extends android.support.v4.app.Fragment {
    private static String USERID;
    private LinearLayout errorPage;
    private LayoutInflater inflater;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor sharePrefEditor = null;

    private static String oldestpostid = "0";
    private static String latestpostid;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerPost;
    private RecyclerView.LayoutManager mLayoutManager;
    private PostAdapter postAdapter;
    private List<Post> posts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //commit latest msg to share preferences
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posts=new ArrayList<>();
        //crete if not exits ,share pref
        sharedPreferences = getActivity().getSharedPreferences("postconfig", Context.MODE_PRIVATE);
        sharePrefEditor = sharedPreferences.edit();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_post);
        recyclerPost = (RecyclerView) view.findViewById(R.id.recycler_post);
        errorPage = (LinearLayout) view.findViewById(R.id.post_err);
        USERID = FunctionsStatic.getUserId(getActivity());

        getPosts(VarStatic.getHostName()+"posts");


        //on post refresh
        recyclerPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        return inflater.inflate(R.layout.post_fragment, container, false);

    }

    private void getPosts(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strreq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Response) {
                        // get response
                        //add json to posts array
                        Log.d("response","post response");

                        try {
                            JSONObject jsonObject=new JSONObject(Response);

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                String id = object.getString("id");
                                String accId = object.getString("acc_id");
                                String accName = object.getString("acc_name");
                                String postBody = object.getString("post_body");
                                String date = object.getString("created_at");
                                String likes = object.getString("likes");
                                String comments = object.getString("comments");
                                String isPhoto = object.getString("isphoto");
                                String liked = object.getString("liked");
                                Post post = new Post(id, accId, accName, postBody, date, comments, likes, isPhoto,
                                        liked);
                                posts.add(post);
                            }
                            posts.add(new Post("id","ac","name","postb","dat","3","3","no","yes"));
                            postAdapter=new PostAdapter(posts,getActivity());
                            recyclerPost.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerPost.setAdapter(postAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Toasty.success(getActivity(), "posts updated!", Toast.LENGTH_SHORT, true).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toasty.error(getActivity(), "error updating posts", Toast.LENGTH_SHORT, true).show();
            }
        });
        queue.add(strreq);
    }

}
