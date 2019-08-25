package com.example.whereisthepotato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private SignInButton signInButton;
    private FirebaseFirestore firestoreDB;

    public void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void setButtons() {
        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.information).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureGoogleSignIn();
        setButtons();
//        firestoreDB = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.authentication_failure), Toast.LENGTH_LONG).show();
            }
        }
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
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.authentication_failure), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void insertUserToFirestore(){
        Toast.makeText(getApplicationContext(), "HEY", Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.information:
                Intent intent2 = new Intent(this, InformationActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user == null)
            return;

        Intent I = new Intent(this, GameActivity.class);
        startActivity(I);
    }

}
