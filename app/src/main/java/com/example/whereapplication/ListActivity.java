package com.example.whereapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;



public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        android.support.v7.widget.Toolbar tlb = findViewById(R.id.toolbarList);
//        setSupportActionBar(tlb);



//
//        Toolbar tlb = findViewById(R.id.toolbarList);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//
//        tlb.setTitle("Lista de Eventos");

//        lvEvents.setAdapter(lvAdapter);
//        dbReference.addChildEventListener(new ChildEventListener() {
//            @Override
//
//
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                list.add(dataSnapshot.getValue(String.class));
//                lvAdapter.notifyDataSetChanged();
//            }
//
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                list.remove(dataSnapshot.getValue(String.class));
//                lvAdapter.notifyDataSetChanged();
//            }
//
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

    }
}




