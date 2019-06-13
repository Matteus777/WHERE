package com.example.whereapplication.ActivitiesClasses;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whereapplication.Object.FineLocation;
import com.example.whereapplication.Object.User;
import com.example.whereapplication.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    final int RC_SIGN_IN = 123;
    final int USER_LOCATION = 1;
    public String searchable;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());
    public String city;
    public String country;
    public String notEventFilter;
    FirebaseDatabase database;
    DatabaseReference dbReference;
    User user1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user1 = new User();
        requestPermimssion();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                    dbReference.child(user.getUid()).setValue(user1);
                    startActivity(intent);
                }else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
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

    public void signOut(View view) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LoginActivity.this, "Usuario deslogado", Toast.LENGTH_SHORT).show();
                finish();
            }
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
                // ...
            } else {
            }


        }
    }
    public static String removeAccents(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }
    public void requestPermimssion() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    USER_LOCATION
            );
        }
        Geocoder geocoder;
        String bestProvider;
        List<Address> user;
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        bestProvider = lm.getBestProvider(criteria, false);
        Location location = lm.getLastKnownLocation(bestProvider);
        if (location == null) {
            Toast.makeText(this, "Location Not found", Toast.LENGTH_LONG).show();

        } else {

            geocoder = new Geocoder(this);
            try {
                String state;
                Log.i("teste",""+location.getLatitude());
                user = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),3);
                Log.i("teste",""+user.get(0).getLatitude());
                country = user.get(0).getCountryCode();
                city  = user.get(0).getSubAdminArea();
                state = user.get(0).getAdminArea();
                Log.i("logtest",country);
                state = filterState(state);
                notEventFilter = city;
                searchable = filterLocation(state,city);
                FineLocation fineLocation = new FineLocation();
                fineLocation.setCity(city);
                fineLocation.setCountry(country);
                fineLocation.setState(state);
                user1.setLocation(fineLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    public static String filterLocation(String state, String city){
        state = removeAccents(state);
        city = removeAccents(city);
        if (state.length()!=2){
            state = filterState(state);

        }

        if(city.contains(" ")){
            city = city.replace(" ","-");
        }
        state = state.toUpperCase();
        String location = city+"-"+state;
        location = location.replace(" ","-");

        return location;
    }

    public static String filterState(String state){
        state = removeAccents(state);
        switch (state){
            case "Acre":
                state = "ac";
                break;
            case "Alagoas":
                state = "al";
                break;
            case "Amapa":
                state = "ap";
                break;
            case "Amazonas":
                state = "am";
                break;
            case "Bahia":
                state = "ba";
                break;
            case "Ceara":
                state = "ce";
                break;
            case "Distrito Federal":
                state = "df";
                break;
            case "Espirito Santo":
                state = "es";
                break;
            case "Goias":
                state = "go";
                break;
            case "Maranhao":
                state = "ma";
                break;
            case "Mato Grosso":
                state = "mt";
                break;
            case "Mato Grosso do Sul":
                state = "ms";
                break;
            case "Minas Gerais":
                state = "mg";
                break;
            case "Para":
                state = "pa";
                break;
            case "Paraiba":
                state = "pb";
                break;
            case "Parana":
                state = "pr";
                break;
            case "Pernambuco":
                state ="pe";
                break;
            case "Piaui":
                state = "pi";
                break;
            case "Rio de Janeiro":
                state = "rj";
                break;
            case "Rio Grande do Norte":
                state = "rn";
                break;
            case "Rio Grande do Sul":
                state = "rs";
                break;
            case "Rondonia":
                state = "ro";
                break;
            case "Roraima":
                state = "rr";
                break;
            case "Santa Catarina":
                state = "sc";
                break;
            case "Sao Paulo":
                state = "sp";
                break;
            case "Sergipe":
                state = "se";
                break;
            case "Tocantins":
                state = "to";
                break;
        }
        return state;
    }
//    private class Description extends AsyncTask<Void, Void, Void>{
//        String url = "https://www.sympla.com.br/eventos/"+searchable;
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                // Connect to the web site
//
//
//                DocumentsContract.Document event = Jsoup.connect(url).get();
//                // Using Elements to get the Meta data
//                Elements mElementDataSize = event.select("div[class=author-date]");
//                // Locate the content attribute
//                int mElementSize = mElementDataSize.size();
//
//                for (int i = 0; i < mElementSize; i++) {
//                    DocumentsContract.Document mElementAuthorName = mBlogDocument.select("span[class=vcard author post-author test]").select("a").eq(i);
//                    String mAuthorName = mElementAuthorName.text();
//
//                    Elements mElementBlogUploadDate = mBlogDocument.select("span[class=post-date updated]").eq(i);
//                    String mBlogUploadDate = mElementBlogUploadDate.text();
//
//                    Elements mElementBlogTitle = mBlogDocument.select("h2[class=entry-title]").select("a").eq(i);
//                    String mBlogTitle = mElementBlogTitle.text();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//        }
//    }

    }




