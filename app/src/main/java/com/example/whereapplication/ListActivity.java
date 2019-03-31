package com.example.whereapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.whereapplication.Object.Event;
import com.example.whereapplication.DAO.DAO;
import com.example.whereapplication.Object.Event;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;



public class ListActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference dbReference;
    ListView lvEvents;
    TextView tvDesc;
    TextView tvTitle;
    TextView tvPrice;
    TextView tvDate;
    TextView tvAddress;
    Bundle extras;
    String location;
    private ArrayList<String> eventNameList;
    private ArrayList<String> eventDateList;
    private ArrayList<String> eventLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent intent = getIntent();
        extras = intent.getExtras();
        location = extras.getString("location");
        new Description().execute();
    }


    public class Description extends AsyncTask<Void, Void, Void> {
        String url = "https://www.sympla.com.br/eventos/" + location;
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Document doc = Jsoup.connect(url).get();
                Elements eventGrid = doc.select("div[class=col-xs-12 col-sm-6 col-md-4 single-event-box]");
                int gridSize = eventGrid.size();
                for (int i = 0; i < gridSize; i++) {
                    Elements eventNameDoc = doc.select("div[class=event-name]").select("p").eq(i);
                    String eventName = eventNameDoc.text();
                    Log.i("teste",eventName);
                    Elements eventMonthDoc = doc.select("div[class=calendar-month]").eq(i);
                    Elements eventDayDoc = doc.select("div[class=calendar-day]").eq(i);
                    Elements eventTimeDoc = doc.select("div[class=line").eq(i);
                    Log.i("teste",eventTimeDoc.toString());
                    String eventDate = eventDayDoc.text() + "/" + eventMonthDoc.text() + " " + eventTimeDoc.text();

                    Elements eventLocationDoc = doc.select("div[class=uperline case]").select("p").eq(i);
                    String eventLocation = eventLocationDoc.text();
                    eventNameList.add(eventName);
                    eventDateList.add(eventDate);
                    eventLocationList.add(eventLocation);

                }
            } catch (Exception e) {
                e.getStackTrace();
            }


            return null;


        }
    }
}


        // --------- FREDERICO ------------//
//
//        ArrayAdapter<Event> EVENTS_LIST_ADAPTER = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, R.id.listEventos);
//
//        ListView listView = (ListView) findViewById(R.id.listEventos);
//        listView.setAdapter(EVENTS_LIST_ADAPTER);


        // --------- Frederico ------------//


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
//        dbReference = FirebaseDatabase.getInstance().getReference()
//                .child("posts");
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Event event = dataSnapshot.getValue(Event.class);
//                tvDate.setText(event.getDate().toString());
//                tvDesc.setText(event.getDesc());
//                tvAddress.setText(event.getLocal());
//                tvPrice.setText(String.valueOf(event.getPrice()));
//                tvTitle.setText(event.getTitle());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                Log.w("loadEvent:onCancelled", databaseError.toException());
//                // [START_EXCLUDE]
//                Toast.makeText(ListActivity.this, "Failed to load event.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        };
//        dbReference.addValueEventListener(eventListener);
//    }






