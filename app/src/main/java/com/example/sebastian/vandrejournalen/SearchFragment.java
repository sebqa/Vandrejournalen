package com.example.sebastian.vandrejournalen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.calendar.CalendarTab;
import com.google.gson.Gson;


public class SearchFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private SearchView searchView;
    User user;
    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance(User user) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("obj" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("obj"), User.class);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        searchView = rootView.findViewById(R.id.search_view);
        searchView.clearFocus();

        loadFragment();
        return rootView;
    }

    private void loadFragment() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_search, CalendarTab.newInstance(user)).commit();

    }

}
