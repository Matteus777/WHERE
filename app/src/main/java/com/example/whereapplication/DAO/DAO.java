package com.example.whereapplication.DAO;

import android.content.Context;

import com.example.whereapplication.Object.Event;
import com.example.whereapplication.Object.Local;
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

    public static void insertLocal(Context context, FirebaseDatabase database, DatabaseReference dbReference, Local local) {
        FirebaseApp.initializeApp(context);

        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("local");
        local.setId(UUID.randomUUID().toString());
        dbReference.setValue(local);

    }



}