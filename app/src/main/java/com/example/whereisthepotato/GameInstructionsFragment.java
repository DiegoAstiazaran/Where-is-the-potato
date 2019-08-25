package com.example.whereisthepotato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;

public class GameInstructionsFragment extends Fragment {

    private int tabPosition;

    private static final String TAB_POSITION = "tab_position";

    public GameInstructionsFragment() {
    }

    public static GameInstructionsFragment newInstance(int position) {
        GameInstructionsFragment fragment = new GameInstructionsFragment();
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
        if (tabPosition == 9) {
            layout = R.layout.free_for_all_instructions;
        } else {
            layout = R.layout.free_for_all_instructions;
        }

        View rootView = inflater.inflate(layout, container, false);

        return rootView;
    }

}

