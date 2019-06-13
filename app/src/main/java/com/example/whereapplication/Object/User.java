package com.example.whereapplication.Object;

import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.database.DataSnapshot;

public class User {
    private FineLocation location;

    public FineLocation getLocation() {
        return location;
    }

    public void setLocation(FineLocation location) {
        this.location = location;
    }

    public static User get(DataSnapshot snapshot){
        User u = new User();
        FineLocation f = new FineLocation();
        f.setState((String)snapshot.child("state").getValue().toString().toUpperCase());
        f.setCity((String)snapshot.child("city").getValue());
        f.setCountry((String)snapshot.child("country").getValue());
        u.setLocation(f);
        return u;
    }
}
