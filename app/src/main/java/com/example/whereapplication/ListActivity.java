package com.example.whereapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        android.support.v7.widget.Toolbar barra = findViewById(R.id.toolbar);
        setSupportActionBar(barra);
    }
}
