package com.example.testingappproject.viewpager;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testingappproject.R;

public class StarterPagerAdapter extends FragmentStateAdapter {
    private final int[] images = {R.drawable.vp_list_icon, R.drawable.vp_motivation_icon, R.drawable.vp_achivements_icon, R.drawable.vp_improvement_icon};
    private final int[] colors = {R.color.pink, R.color.view_pager_green};
    private final String[] headlines;
    private final String[] descriptions;

    public StarterPagerAdapter(FragmentActivity fa, Context context) {
        super(fa);

        Resources resources = context.getResources();
        headlines = resources.getStringArray(R.array.view_pager_headlines);
        descriptions = resources.getStringArray(R.array.view_pager_descriptions);
    }

    @Override
    public Fragment createFragment(int position) {
        Bundle arguments = new Bundle();
        arguments.putInt(UniversalFragment.COLOR, colors[position%2]);
        if (position == 0){
            WelcomeFragment fragment = new WelcomeFragment();
            fragment.setArguments(arguments);
            return fragment;
        }else if(position == getItemCount() -1){
            FinishFragment fragment = new FinishFragment();
            fragment.setArguments(arguments);
            return fragment;
        }
        arguments.putString(UniversalFragment.VIEWPAGER_HEADLINES, headlines[position-1]);
        arguments.putString(UniversalFragment.VIEWPAGER_DESCRIPTIONS, descriptions[position-1]);
        arguments.putInt(UniversalFragment.VIEWPAGER_IMAGE, images[position-1]);

        UniversalFragment universalFragment = new UniversalFragment();
        universalFragment.setArguments(arguments);

        return universalFragment;
    }

    @Override
    public int getItemCount() {
        return headlines.length + 2;
    }
}
