package com.example.whereisthepotato;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class JoinRoomActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room_new);

        Button btnJoinRoom = findViewById(R.id.join_room);

        btnJoinRoom.setOnClickListener(this);

        firestoreDB = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        final EditText etRoomName = findViewById(R.id.room_name);
        switch (view.getId()) {
            case R.id.join_room:
                if(TextUtils.isEmpty(etRoomName.getText())) {
                    Toast.makeText(this.getApplicationContext(), "A name for the room is required!", Toast.LENGTH_SHORT).show();
                } else {
                    firestoreDB.collection("games")
                            .whereEqualTo("name", etRoomName.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String gameId = "";
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            gameId = document.getId();
                                        }
                                        if (gameId.isEmpty()) {
                                            Toast.makeText(getApplicationContext(), "Room not found", Toast.LENGTH_SHORT).show();
                                        } else {
                                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                            FirebaseUser fbuser = firebaseAuth.getCurrentUser();
                                            String fbid = fbuser.getUid();
                                            DocumentReference gameToJoin = firestoreDB.collection("games").document(gameId);
                                            gameToJoin.update("players", FieldValue.arrayUnion(fbid));
                                            Toast.makeText(getApplicationContext(), "Joined " + etRoomName.getText().toString() + " room!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
                break;
            default:
                break;
        }
    }
}
