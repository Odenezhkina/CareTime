package com.example.testingappproject.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testingappproject.R;


public class UniversalFragment extends Fragment {
    public static final String VIEWPAGER_HEADLINES = "view_pager_headlines";
    public static final String VIEWPAGER_DESCRIPTIONS = "view_pager_descriptions";
    public final static String VIEWPAGER_IMAGE = "viewpager image";
    public final static String COLOR = "color";
    private String curHeadline;
    private String curDescription;
    private int curImageId;
    private TextView tvHeadline;
    private ImageView ivPicture;
    private TextView tvDescription;
    private LinearLayout ll;
    private int color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_universal, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            curHeadline = arguments.getString(VIEWPAGER_HEADLINES);
            curDescription = arguments.getString(VIEWPAGER_DESCRIPTIONS);
            curImageId = arguments.getInt(VIEWPAGER_IMAGE);
            color = arguments.getInt(COLOR);
        }

        tvHeadline = view.findViewById(R.id.uf_headline);
        ivPicture = view.findViewById(R.id.uf_image_view);
        tvDescription = view.findViewById(R.id.uf_description);
        ll = view.findViewById(R.id.fu_ll);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        display();
    }

    private void display() {
        tvHeadline.setText(curHeadline);
        ivPicture.setImageResource(curImageId);
        tvDescription.setText(curDescription);
        fadeOut();
        ll.setBackgroundColor(getResources().getColor(color));
    }

    private void fadeOut() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 100, 0);
        animation.setDuration(1000);
        ivPicture.startAnimation(animation);
    }
}