package com.example.hw9;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchlistFragment newInstance(String param1, String param2) {
        WatchlistFragment fragment = new WatchlistFragment();
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

    private void resetWatchList(ArrayList<MovieData> watchList){
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String key = "watchlist";
        String arr_builder = "";
        for(int i = 0; i < watchList.size() ; i++){
            String val = watchList.get(i).getId() + "," + watchList.get(i).getMedia_type() + "," + watchList.get(i).getImgUrl() + "," + watchList.get(i).getTitle();
            arr_builder = arr_builder + val + "#";
        }
        editor.putString(key,arr_builder);
        editor.commit();
    }

    ArrayList<MovieData> watchList = new ArrayList<>();
    View rootView;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(watchList,fromPosition,toPosition);

            recyclerView.getAdapter().notifyDataSetChanged();
//            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
//            recyclerView.getAdapter().notifyItemRangeChanged(0,watchList.size());
            resetWatchList(watchList);

            return true;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

//        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_watchlist, container, false);
        try {
            String key = "watchlist";
            String main_arr_str = pref.getString(key,null);
            System.out.println("Before in WatchList Fragment: " + main_arr_str);
            if(main_arr_str != null && main_arr_str.length() != 0){
                String[] arr = main_arr_str.split("#");
                for(int j = 0 ; j < arr.length ; j++) {
                    String[] temp = arr[j].split(",");
                    watchList.add(new MovieData(temp[2],temp[0],temp[1],temp[3]));
                }
            }
            else{
                TextView watchListTxt = rootView.findViewById(R.id.nilWatchListText);
                watchListTxt.setVisibility(View.VISIBLE);
            }


            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.watchListRecycler);
            // set a GridLayoutManager with default vertical orientation and 2 number of columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
            recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            //  call the constructor of CustomAdapter to send the reference and data to Adapter
            WatchListAdapter customAdapter = new WatchListAdapter(getContext(), watchList, rootView);
            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        catch (Exception e){
            System.out.println(e);
        }

        return rootView;
    }
}