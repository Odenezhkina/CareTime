package com.example.testingappproject.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.testingappproject.MainActivity;
import com.example.testingappproject.R;

public class WelcomeFragment extends Fragment {
    private ConstraintLayout cl;
    private int color;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments != null){
            color = arguments.getInt(UniversalFragment.COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        cl = view.findViewById(R.id.fw_cl);
        setBackgroundColor();

        Button btnSkip = view.findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener((v) -> {
            MainActivity.mPager.setVisibility(View.GONE);
            MainActivity.bnv.setVisibility(View.VISIBLE);
            MainActivity.fr.setVisibility(View.VISIBLE);
        });
        return view;
    }

    private void setBackgroundColor(){
        cl.setBackgroundColor(getResources().getColor(color));
    }
}