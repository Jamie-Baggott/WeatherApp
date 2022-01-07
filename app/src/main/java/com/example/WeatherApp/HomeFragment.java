package com.example.WeatherApp;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button welcome = view.findViewById(R.id.welcomeButton);
        welcome.setOnClickListener(v -> {
            Intent welcome1 = new Intent(getActivity(), DetailsActivity.class);
            startActivity(welcome1);
        });

        return view;
    }
}