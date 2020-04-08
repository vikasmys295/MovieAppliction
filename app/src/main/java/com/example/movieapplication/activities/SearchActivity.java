package com.example.movieapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapplication.R;

import com.example.movieapplication.adapters.SearchAdapter;
import com.example.movieapplication.model.Results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity  {

    List<Results> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    RequestQueue requestQueue;
    String url = "https://api.themoviedb.org/3/discover/movie?api_key=c2e0d1ac159c856cd8e448bab8a71c68&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //getting references and setting recyclerview adapter
        requestQueue = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter(movieList,this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(searchAdapter);

        getData(url);
    }

    //Adding searchview in the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
           //filtering the text in searchview
            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<Results> newList = new ArrayList<>();
                for(Results movie : movieList){
                    if(movie.getTitle().toLowerCase().contains(userInput)){
                        newList.add(movie);
                    }
                }
                searchAdapter.updateList(newList);
                return true;
            }
        });
        return true;
    }

    //getting data for recyclerview
    private void getData(String url) {
        //adding progressDialog for the delay in response
        final ProgressDialog progressDialog = new ProgressDialog(this);
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
                searchAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley test", error.toString());
                Toast.makeText(getApplicationContext(), "volley error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}