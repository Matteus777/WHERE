package com.example.whereapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whereapplication.DAO.DAO;
import com.example.whereapplication.Object.Event;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    final int RC_SIGN_IN = 123;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());
    AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
            .Builder(R.layout.activity_login)
            .setFacebookButtonId(R.id.fbButton)
                .setPhoneButtonId(R.id.phoneButton)
                .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
               auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(LoginActivity.this,ListActivity.class);
                    startActivity(intent);
                }else{
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setAuthMethodPickerLayout(customLayout)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authListener);
    }
    public void signOut(View view){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LoginActivity.this, "Usuario deslogado", Toast.LENGTH_SHORT).show();
           finish(); }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(LoginActivity.this,ListActivity.class);
                startActivity(intent);
                // ...
            } else {

            }
        }
    }
}
