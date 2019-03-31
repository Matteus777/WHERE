package com.example.whereapplication.DAO;

import android.content.Context;

import com.example.whereapplication.Object.Event;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class DAO {


    public static void insertEvent(Context context, FirebaseDatabase database, DatabaseReference dbReference, Event event) {
        FirebaseApp.initializeApp(context);
        database = FirebaseDatabase.getInstance();
        event.setId(UUID.randomUUID().toString());
        dbReference.child("event").child(event.getId()).setValue(event);


    }




}