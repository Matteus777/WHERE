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
        new Search().execute();

    }

    public class Search extends AsyncTask<Void, Void, Void> {
        String eventLocation;
        String eventName;
        String eventDate;
        String eventPrice;
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
        int currentEvent;
        Event[]eventList  = new Event[630];


        @Override
        public Void doInBackground(Void... voids) {
            filter = "musica";
            for(int p = 0;p<eventList.length;p++){
                
            }
            try {
                currentEvent=0;
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
                                                Elements eventPriceSizeElement = eventCell.select("form#ticket-form").select("tr");
                                                eventPriceSize = eventPriceSizeElement.size();
                                                Price[] priceObj = new Price[eventPriceSize];
                                            }catch (Exception e) {
                                                e.getStackTrace();
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
//                                                Log.i("teste_free",String.valueOf(eventPriceNum));

                                            }
                                            else {
                                                Elements eventPriceExists = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td").eq(0).select("span").eq(1);
                                                    String price = eventPriceExists.text().substring(3);
                                                    price = price.replaceAll(",", ".");
                                                    eventPriceNum = Double.valueOf(price);
                                                    Log.i("teste_preco", String.valueOf(eventPriceNum));
                                                    eventPriceNum


                                                }


//                                            Elements eventPriceDoc1 = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td").eq(0).select("span").eq(0);
////                                          priceObj[p].setLote(eventPriceDoc1.text());
////                                          priceObj[p].setValue(eventPriceNum);
////                                          eventList[j][w].setPrice(priceObj);
                                        }
                                    }


                }
            }
            }catch (Exception e) {
                e.getStackTrace();
            }


                    try{
                        for (int k = 1; k < 30; k++) {
//                                   Log.i("testin", "page" + k);
                            String url = "https://www.sympla.com.br/eventos/"+location+"?s="+filter+"&pagina="+k;
                            Document docs = Jsoup.connect(url).get();
                            Elements ifExists = docs.normalise().select("h3[class=pull-left]");
                            if (!ifExists.isEmpty()) {
                                break;
                            }
                            Elements eventGrid = docs.select("div[class=col-xs-12 col-sm-6 col-md-4 single-event-box]");
                            int gridSize = eventGrid.size();
                            for (int i = 0; i < gridSize; i++) {
                                Elements eventLocationDoc = docs.select("div[class=uppercase line]").select("p").eq(i);
                                eventLocation = eventLocationDoc.text();
                                Elements eventNameDoc = docs.select("div[class=event-name]").select("p").eq(i);
                                eventName = eventNameDoc.text();
                                Elements eventMonthDoc = docs.select("div[class=calendar-month]").eq(i);
                                Elements eventDayDoc = docs.select("div[class=calendar-day]").eq(i);
                                Elements eventTimeDoc = docs.normalise().select("div[class=line]").not("i").eq(i);
                                eventDate = eventDayDoc.text() + "/" + eventMonthDoc.text() + " " + eventTimeDoc.text();

                            }
                        }
                    }catch(Exception e ){
                        e.getStackTrace();
                    }
            return null; }
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






































