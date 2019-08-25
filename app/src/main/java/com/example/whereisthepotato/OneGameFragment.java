package com.example.whereisthepotato;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

import static android.content.ContentValues.TAG;

public class OneGameFragment extends Fragment {

    private int tabPosition;

    private static final String TAB_POSITION = "tab_position";

    private FirebaseAuth firebaseAuth;

    public OneGameFragment() {
    }

    public static OneGameFragment newInstance(int position) {
        OneGameFragment fragment = new OneGameFragment();
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
        View rootView = inflater.inflate(R.layout.game_detail, container, false);

        Toast toast = Toast.makeText(getContext(), "Si esta aqui", Toast.LENGTH_SHORT);


        // TODO: load details into view; get them from firebase


        return rootView;
    }



}

