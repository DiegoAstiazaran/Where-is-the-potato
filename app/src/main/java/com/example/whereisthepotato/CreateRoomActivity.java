package com.example.whereisthepotato;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CreateRoomActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        Button btPrivateType = findViewById(R.id.private_type);
        Button btPublicType = findViewById(R.id.public_type);
        Button btCreateRoom = findViewById(R.id.create_room);

        btPrivateType.setOnClickListener(this);
        btPublicType.setOnClickListener(this);
        btCreateRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        TextView tvPassword = findViewById(R.id.password_text);
        EditText etPassword = findViewById(R.id.password_input);
        EditText etRoomName = findViewById(R.id.room_name);
        RadioButton rbPrivate = findViewById(R.id.private_type);
        switch (view.getId()) {
            case R.id.public_type:
                tvPassword.setVisibility(View.INVISIBLE);
                etPassword.setVisibility(View.INVISIBLE);
                break;
            case R.id.private_type:
                tvPassword.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                break;
            case R.id.create_room:
                if(rbPrivate.isChecked() && TextUtils.isEmpty(etPassword.getText())) {
                    Toast.makeText(this.getApplicationContext(), "A password is required for private rooms!", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(etRoomName.getText())) {
                    Toast.makeText(this.getApplicationContext(), "A name for the room is required!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
