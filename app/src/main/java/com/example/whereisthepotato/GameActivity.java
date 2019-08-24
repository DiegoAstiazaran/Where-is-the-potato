package com.example.whereisthepotato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity implements  View.OnClickListener {

    public Button btCreateRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btCreateRoom = findViewById(R.id.create_game);

        btCreateRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_game:
                Intent intent = new Intent(view.getContext(), CreateRoomActivity.class);
                view.getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }


}
