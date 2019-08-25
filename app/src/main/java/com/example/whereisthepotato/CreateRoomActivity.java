package com.example.whereisthepotato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Date;

import org.w3c.dom.Text;

import java.util.Calendar;

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
        RadioButton rbPublic = findViewById(R.id.public_type);
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
                if(TextUtils.isEmpty(etRoomName.getText())) {
                    Toast.makeText(this.getApplicationContext(), "A name for the room is required!", Toast.LENGTH_SHORT).show();
                } else if(rbPrivate.isChecked() && TextUtils.isEmpty(etPassword.getText())) {
                    Toast.makeText(this.getApplicationContext(), "A password is required for private rooms!", Toast.LENGTH_SHORT).show();
                } else {
                    RadioGroup rgType = findViewById(R.id.type_options);
                    RadioButton checked = rgType.findViewById(rgType.getCheckedRadioButtonId());
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    CollectionReference games = database.collection("games");
                    User user = new User(currentUser.getEmail(), currentUser.getDisplayName(),
                            null, null, null);
                    Room room = new Room(etRoomName.getText().toString(), checked.getText().toString().toLowerCase(),
                            Calendar.getInstance().getTime(), null, 10.0, true,
                            100.0, null, user, etPassword.getText().toString());
                    games.add(room);
                    Intent intent = new Intent(view.getContext(), GameActivity.class);
                    view.getContext().startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
