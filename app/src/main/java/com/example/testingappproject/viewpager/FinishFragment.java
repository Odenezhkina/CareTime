package com.example.testingappproject.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.testingappproject.MainActivity;
import com.example.testingappproject.R;

public class FinishFragment extends Fragment {
    private ConstraintLayout cl;
    private int color;
    private Button btnStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            color = arguments.getInt(UniversalFragment.COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish, container, false);
        cl = view.findViewById(R.id.ff_cl);
        btnStart = view.findViewById(R.id.btn_start);
        setBackgroundColor();
        startTVAnimation();

        btnStart.setOnClickListener((v) -> {
            MainActivity.mPager.setVisibility(View.GONE);
            MainActivity.bnv.setVisibility(View.VISIBLE);
            MainActivity.fr.setVisibility(View.VISIBLE);
        });
        return view;
    }

    private void startTVAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(-300, 0, 0, 0);
        translateAnimation.setDuration(1000);
        btnStart.startAnimation(translateAnimation);
    }

    private void setBackgroundColor() {
        cl.setBackgroundColor(getResources().getColor(color));
    }
}