package com.example.whereisthepotato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btLogIn = findViewById(R.id.log_in);
        btLogIn.setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.information);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_in:
                Intent intent1 = new Intent(this, GameActivity.class);
                startActivity(intent1);
                break;
            case R.id.information:
                Intent intent2 = new Intent(this, InformationActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
