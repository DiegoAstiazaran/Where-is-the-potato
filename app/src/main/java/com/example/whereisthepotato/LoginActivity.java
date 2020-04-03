package com.example.whereisthepotato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whereisthepotato.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;

    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestoreDB;
    private User user;

    private int RC_SIGN_IN = 0;

    public void configureGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void setButtons() {
        binding.signInButton.setOnClickListener(this);
        binding.information.setOnClickListener(this);
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
        if (user == null)
            return;

        Intent I = new Intent(this, OneGameActivity.class);
        startActivity(I);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("Firebase Auth", account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success
                            Log.d("Firebase Auth", "signInWithCredential:success");
                            currentUser = firebaseAuth.getCurrentUser();
                            if(currentUser != null) {
                                insertUserToFirestore();
                                String toastMessage = String.format(getResources().getString(R.string.welcome_message), currentUser.getDisplayName());
                                Toast.makeText(getApplicationContext(), toastMessage , Toast.LENGTH_LONG).show();
                                updateUI(currentUser);
                            } else {
                                Log.d("Firebase User", "No current user");
                            }
                        } else {
                            Log.d("Firebase Auth", "signInWithCredential:failure");
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.authentication_failure_firebase), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void insertUserToFirestore(){
        firestoreDB.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(!document.exists()) {
                        user = new User(currentUser.getDisplayName(), currentUser.getEmail());
                        firestoreDB.collection("users").document(currentUser.getUid()).set(user);
                        updateUI(currentUser);
                        Log.i("Insert user:", "User inserted");
                    } else {
                        Log.i("Insert user:", "User already exists");
                        updateUI(currentUser);
                    }
                } else {
                    Log.d("Get User:", "Task unsuccessful");
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result from sign in intent
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google SignIn", "Google sign in failed", e);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.authentication_failure_google), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.information:
                startActivity(new Intent(this, InformationActivity.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureGoogleSignIn();
        setButtons();
        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }
}