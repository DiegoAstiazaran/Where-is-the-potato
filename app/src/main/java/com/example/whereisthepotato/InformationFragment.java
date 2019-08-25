package com.example.whereisthepotato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class InformationFragment extends Fragment {

    private int tabPosition;

    private static final String TAB_POSITION = "tab_position";

    public InformationFragment() {
    }

    public static InformationFragment newInstance(int position) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            tabPosition = bundle.getInt(TAB_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout;
        if (tabPosition == 0) {
            layout = R.layout.free_for_all_instructions;
        } else {
            layout = R.layout.free_for_all_instructions;
        }

        View rootView = inflater.inflate(layout, container, false);

        return rootView;
    }

}

