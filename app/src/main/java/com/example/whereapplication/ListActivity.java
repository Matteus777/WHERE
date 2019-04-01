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
import android.widget.ThemedSpinnerAdapter;
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

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
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
        String eventLocation;
        String eventName;
        String eventDate;
        String eventPrice;
        Event event = new Event();
        String cellUrl;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int j = 0; j < 30; j++) {
                    String url = "https://www.sympla.com.br/eventos/" + location + "?ordem=data&pagina=" + j;
//              Log.i("teste","page"+j);
                    final Document doc = Jsoup.connect(url).get();
                    Elements ifExists = doc.normalise().select("h3[class=pull-left]");
                    if (!ifExists.isEmpty()) {
                        continue;
                    }
                    Elements eventGrid = doc.select("div[class=col-xs-12 col-sm-6 col-md-4 single-event-box]");
                    final int gridSize = eventGrid.size();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0; i < gridSize; i++) {
                                    Log.i("testao",String.valueOf(i));
                                    cellUrl = doc.select("a.event-box-link").attr("href");
                                    Document eventCell = Jsoup.connect(cellUrl).get();
                                    Elements eventPriceDoc = eventCell.select("tbody");
                                    eventPrice = eventPriceDoc.text();
                                    event.setPrice(eventPrice);
                                    Log.i("teste","price"+i);
                                }
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < gridSize; i++) {
                                Elements eventLocationDoc = doc.select("div[class=uppercase line]").select("p").eq(i);
                                eventLocation = eventLocationDoc.text();
                                Elements eventNameDoc = doc.select("div[class=event-name]").select("p").eq(i);
                                eventName = eventNameDoc.text();
                                Elements eventMonthDoc = doc.select("div[class=calendar-month]").eq(i);
                                Elements eventDayDoc = doc.select("div[class=calendar-day]").eq(i);
                                Elements eventTimeDoc = doc.normalise().select("div[class=line]").not("i").eq(i);
                                eventDate = eventDayDoc.text() + "/" + eventMonthDoc.text() + " " + eventTimeDoc.text();
                                event.setDate(eventDate);
                                event.setLocal(eventLocation);
                                event.setTitle(eventName);
//                                Log.i("teste","event"+ finalJ);
                            }
                        }
                    }).start();
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