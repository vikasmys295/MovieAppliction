package com.example.movieapplication.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapplication.R;
import com.example.movieapplication.adapters.PopularMovieAdapter;
import com.example.movieapplication.model.Results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopularMoviesFragment extends Fragment {

    List<Results> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    PopularMovieAdapter recyclerAdapter;
    RequestQueue requestQueue;
    Context context;
    ProgressDialog progressDialog;
    String url ="https://api.themoviedb.org/3/discover/movie?api_key=c2e0d1ac159c856cd8e448bab8a71c68&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getting volley references
      requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
      getData(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        // getting references and set recyclerview adapter
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new PopularMovieAdapter(movieList,getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    private void getData(String url) {
        //adding progressDialog for the delay in response
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        //volley get request and json parsing
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataResults = jsonArray.getJSONObject(i);
                            String name = dataResults.getString("title");
                            String poster = dataResults.getString("poster_path");
                            Results movieResults = new Results();
                            movieResults.setTitle(name);
                            movieResults.setPoster_path(poster);
                            movieList.add(movieResults);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    recyclerAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volley test", error.toString());
                    progressDialog.dismiss();
                }
            });
            requestQueue.add(request);
        }
}
