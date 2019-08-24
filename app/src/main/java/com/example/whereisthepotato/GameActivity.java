package com.example.whereisthepotato;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    public Button btCreateRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btCreateRoom = findViewById(R.id.create_game);

//        btCreateRoom.setOnClickListener(this.);
    }


}
