package com.example.whereapplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whereapplication.DAO.DAO;

import com.example.whereapplication.Object.Event;
import com.example.whereapplication.Object.Local;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private int LOCATION_PERMISSION_CODE = 1;


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks pCallBacks;
    private CallbackManager lgnCallBack;
    LoginButton lgnFacebook;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        lgnCallBack.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lgnFacebook = (LoginButton) findViewById(R.id.fbLogin);
        lgnCallBack = CallbackManager.Factory.create();
        lgnFacebook.setReadPermissions("public_profile");
        lgnFacebook.registerCallback(lgnCallBack, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accesstoken = loginResult.getAccessToken();
                GraphRequest graphrequest = GraphRequest.newMeRequest(accesstoken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Toast.makeText(LoginActivity.this,"WAD", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                        startActivity(intent);
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("fields","id");
                graphrequest.setParameters(bundle);
                graphrequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
            }
        });



        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestLocationPermission();
        }

    }
    private void displayData(JSONObject object){

    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }
}


