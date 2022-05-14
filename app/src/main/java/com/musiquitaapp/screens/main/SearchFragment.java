package com.musiquitaapp.screens.main;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.musiquitaapp.R;
import com.musiquitaapp.adapters.SearchAdapter;
import com.musiquitaapp.databinding.FragmentSearchBinding;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private ArrayList<Items> items;
    private RecyclerView searchRecycler;
    private SearchAdapter myAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = -1;
        try {
            position = myAdapter.getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.addPlaylist:
                Toast.makeText(getContext(), "Añadir a Playlists", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addQueue:
                Toast.makeText(getContext(), "Añadir a Cola", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        // TODO Fragment cosas

        binding.searchbar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                searchContent();
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });



        return view;


    }

    private void searchContent() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Config.YOUTUBE_QUERY_VIDEO + binding.searchbar.getText() + Config.YOUTUBE_QUERY_VIDEO_2 + Config.YOUTUBE_API_KEY,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String num_results = response.optString("resultsPerPage");

                        JSONArray jsonArray = response.optJSONArray("items");
                        List<Items> results = Arrays.asList(new GsonBuilder().create().fromJson(jsonArray.toString(), Items[].class));

                        myAdapter = new SearchAdapter(results, getContext());
                        binding.searchRecycler.setAdapter(myAdapter);
                        binding.searchRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("tag", "onErrorResponse: " + error.getMessage());
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }
}