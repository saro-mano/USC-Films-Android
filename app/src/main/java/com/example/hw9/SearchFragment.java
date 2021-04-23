package com.example.hw9;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View rootView;


    private void displaySearch(ArrayList searchList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = rootView.findViewById(R.id.searchRecycle);
        recyclerView.setLayoutManager(layoutManager);
        SearchAdapter adapter1 = new SearchAdapter(getContext(),searchList);
        recyclerView.setAdapter(adapter1);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = rootView.findViewById(R.id.search_view);



        searchView.setQueryHint("Search movies and TV");
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                return true;
            }

            @Override
            public boolean onQueryTextChange(String input) {
                System.out.println(input);
                String url = "http://10.0.2.2:8080/getSearchResults?inp="+input;
                RequestQueue queue = Volley.newRequestQueue(getActivity());

                ArrayList<SearchData> searchDataList = new ArrayList<>();
                JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Display the first 500 characters of the response string.
                                try {
                                    System.out.println(response);
                                    for(int i = 0 ; i < response.length() ; i++){
                                        JSONObject detail = response.getJSONObject(i);
                                        String title = detail.getString("title");
                                        String media_type = detail.getString("media_type");
                                        String rating = detail.getString("rating");
                                        String background = detail.getString("poster_path");
                                        String id = detail.getString("id");
                                        String year = detail.getString("year");
                                        searchDataList.add(new SearchData(id,title,media_type,background,rating,year));
                                        System.out.println(background);
                                    }
                                    displaySearch(searchDataList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(jsonRequest);

                return false;
            }
        });
        return rootView;
    }
}