package com.example.sebastian.vandrejournalen.calendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;


public class SearchFragment extends Fragment {

    User user;
    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get User object from Bundle
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("user"), User.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        SearchView searchView = rootView.findViewById(R.id.search_view);
        searchView.clearFocus();

        loadFragment();
        return rootView;
    }

    private void loadFragment() {
        FragmentManager fm = getFragmentManager();
        //Create fragment instance
        CalendarTab fragment = CalendarTab.newInstance();
        //Add object as json string to bundle
        Bundle args = new Bundle();
        String obj = new Gson().toJson(user);
        args.putString("user" , obj);
        fragment.setArguments(args);
        fm.beginTransaction().replace(R.id.content_search, fragment).commit();

    }

}
