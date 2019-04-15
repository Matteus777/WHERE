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
import com.example.whereapplication.Object.Price;
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
import java.security.Timestamp;
import java.sql.Date;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
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
        new Search().execute();

    }

    public class Search extends AsyncTask<Void, Void, Void> {
        String eventLocation;
        String eventName;
        String eventDate;
        String[] cellUrl;
        String filter;
        int eventPriceSize;
        Document doc;
        int j;
        Elements eventGrid;
        int p;
        Document eventCell;
        int w;
        public TextView tvFilter;
        //      public String cellEvent;
        int eventCount;
        int i;
        String url;
        private DatabaseReference firebaseDatabase;
        int currentEvent;
        Event[]eventList  = new Event[630];


        @Override
        public Void doInBackground(Void... voids) {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
            filter = "musica";
            for(int p = 0;p<eventList.length;p++){
               Event e = new Event();
                eventList[p]=e;
            }
            try {
                currentEvent=0;
                Log.i("teste", "tempo: " + Calendar.getInstance().getTime().getTime() );
            //    Log.i("teste","time: "+Timestamp.parse("2019-04-15 14:00:00"));
                for (j = 1; j < 30; j++) {
                    Log.i("teste","page"+j);
                    url = "https://www.sympla.com.br/eventos/"+location+"?s="+filter+"&pagina="+j;
                    doc = Jsoup.connect(url).get();
                    Elements ifExists = doc.normalise().select("h3[class=pull-left]");
                    if (!ifExists.isEmpty()) {
                        break;
                    }
                    Elements eventGrid = doc.select("div[class=col-xs-12 col-sm-6 col-md-4 single-event-box]");
                    int gridSize = eventGrid.size();
                    cellUrl = new String[gridSize];
                    for ( i = 0; i < gridSize; i++) {
                        cellUrl[i] = doc.select("a.event-box-link").eq(i).attr("href");
                    }
                    for(final String cellEvent:cellUrl){
                        Log.i("teste_url",cellEvent);
                        try {
                            eventCell  = Jsoup.connect(cellEvent).get();
                            Elements eventNameElement = eventCell.select("h1.event-name");
                            Elements eventLocationElement = eventCell.normalise().select("div.event-info-city");
                            Elements eventDateElement = eventCell.normalise().select("div.event-info-calendar");

                            eventName = eventNameElement.text();
                            eventLocation = eventLocationElement.text();
                            eventDate = eventDateElement.text();
                            if(eventName.isEmpty()){
                                eventName = eventCell.select("h1.uppercase").text();
                            }
                            Log.i("teste",eventName);













                            Elements eventPriceSizeElement = eventCell.select("form#ticket-form").select("tr");
                            eventPriceSize = eventPriceSizeElement.size();

                        }catch (Exception e) {
                            e.getStackTrace();
                        }
                        Price[] priceObj = new Price[eventPriceSize];
                        for(int p = 0;p<priceObj.length;p++){
                            Price price = new Price();
                            priceObj[p]=price;

                        }
                        for( p = 1; p<eventPriceSize;p++) {
                            double eventPriceNum;
                            Elements eventPriceCheck = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td.opt-panel");
                            if(eventPriceCheck.text().contains("Esgotado")||eventPriceCheck.text().contains("Encerrado")||eventPriceCheck.text().contains("Não iniciado")){

                            }
                            else {
                                Elements eventPrice = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td").eq(0);
                                String free = eventPrice.text().toLowerCase();
                                free =  LoginActivity.removeAccents(free);
//                                        Log.i("teste",free);
                                if (free.contains("gratis")) {
                                    eventPriceNum = 0;
                                    priceObj[p-1].setValue(eventPriceNum);
                                    Elements eventPriceDoc1 = eventPrice.select("span").eq(0);
                                    priceObj[p].setLote(eventPriceDoc1.text());
                                    Log.i("teste",String.valueOf(eventPriceDoc1.text()));
                                    eventList[currentEvent].setPrice(priceObj);
                                }
                                else {
                                    Elements checkID = eventCell.normalise().select("form#ticket-form").select("tr").eq(p);
                                    if(checkID.attr("id").equals("show-discount")||checkID.attr("id").equals("discount-form")){
                                    }else{
                                        Elements eventPriceExists = checkID.select("td").eq(0).select("span").eq(1);
                                        String price = eventPriceExists.text().substring(3);
                                        price = price.replaceAll(",", ".");
                                        eventPriceNum = Double.valueOf(price);
                                        priceObj[p].setValue(eventPriceNum);
                                        Elements eventPriceDoc1 = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td").eq(0).select("span").eq(0);
                                        priceObj[p].setLote(eventPriceDoc1.text());
                                        eventList[currentEvent].setPrice(priceObj);
                                    }
                                }
                            }
                        }

                        currentEvent++;
                    }
                }
            }catch (Exception e) {
                e.getStackTrace();
            }
            return null; }

        private String getMonth(String month){
            switch(month){
                case "janeiro":month = "1";
                    break;
                case "fevereiro":month = "2";
                    break;
                case "março":month ="3";
                    break;
                case "abril":month = "4";
                    break;
                case "maio":month = "5";
                    break;
                case "junho":month = "6";
                    break;
                case "julho":month = "7";
                    break;
                case "agosto":month = "8";
                    break;
                case "setembro":month = "9";
                    break;
                case "outubro":month = "10";
                    break;
                case "novembro":month = "11";
                    break;
                case "dezembro":month = "12";

            }
            return month;
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






































