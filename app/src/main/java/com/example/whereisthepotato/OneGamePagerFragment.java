package com.example.whereisthepotato;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.whereisthepotato.databinding.FragmentGamesPagerBinding;

public class OneGamePagerFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private FragmentGamesPagerBinding binding;

    public OneGamePagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentGamesPagerBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = binding.viewPager;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // TODO: intialize position with another value if getting from 'all games' list
        mViewPager.setCurrentItem(0);

        binding.allGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a MaterialListFragment
                return OneGameFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // TODO: Get this number from firebase!!!
            int user_games_count = 3;
            return user_games_count;
        }
    }

}
