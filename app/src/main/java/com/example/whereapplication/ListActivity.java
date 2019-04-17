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
import java.sql.Time;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;


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
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
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
        int min;
        Timestamp dateStamp;
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
                    Log.i("teste","a");
                    cellUrl = new String[gridSize];
                    for ( i = 0; i < gridSize; i++) {
                        Elements singleEvent = doc.select("a.event-box-link").eq(i);
                        cellUrl[i] = doc.select("a.event-box-link").eq(i).attr("href");
                        Elements eventLocationElement = singleEvent.select("div.uppercase.line").select("p");
                        Elements eventNameElement = singleEvent.select("div.event-name").select("p");
                        eventLocation = eventLocationElement.text();
                        eventName = eventNameElement.text();
                        eventList[currentEvent].setTitle(eventName);
                        eventList[currentEvent].setLocal(eventLocation);












                        int eventDay = Integer.parseInt(singleEvent.select("div.calendar-day").text());
                        int eventMonth = Integer.parseInt(getMonth(singleEvent.select("div.calendar-month").text()));
                        int hour = Integer.parseInt(singleEvent.select("div.line").not(".uppercase").text().substring(0,2));
                        int sec = 0;
                        String checkMin = singleEvent.select("div.line").not(".uppercase").text().substring(3,4);
                        checkMin = checkMin.replace(" ","");
                        if(!checkMin.isEmpty()){
                            min = Integer.parseInt(singleEvent.select("div.line").not(".uppercase").text().substring(3,5));
                        }else{
                            min = 00;
                        }
                        Calendar date = Calendar.getInstance();
                        date.set(2019, eventMonth-1,eventDay,hour,min);
                        dateStamp = new Timestamp(date.getTime().getTime());
                        eventList[currentEvent].setDate(dateStamp);
                        dbReference.child(location).child(filter).child(eventList[currentEvent].getTitle()).setValue(eventList[currentEvent]);
                    }
                    for(final String cellEvent:cellUrl){
                        Log.i("teste_url",cellEvent);
                        try {

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
                case "Jan":month = "1";
                    break;
                case "Feb":month = "2";
                    break;
                case "Mar":month ="3";
                    break;
                case "Abr":month = "4";
                    break;
                case "Mai":month = "5";
                    break;
                case "Jun":month = "6";
                    break;
                case "Jul":month = "7";
                    break;
                case "Ago":month = "8";
                    break;
                case "Set":month = "9";
                    break;
                case "Out":month = "10";
                    break;
                case "Nov":month = "11";
                    break;
                case "Dez":month = "12";

            }
            return month;
        }
        private String getMonthAgain(String month){
            if(month.contains("janeiro")){
                month = "janeiro";

            }else{
                if(month.contains("fevereiro")){
                    month = "fevereiro";
                }else{
                    if(month.contains("março")){
                        month = "março";
                    }else{
                        if(month.contains("abril")){
                            month = "abril";
                        }else{
                            if(month.contains("maio")){
                                month = "maio";
                            }else{
                                if(month.contains("junho")){
                                    month = "junho";
                                }else{
                                    if(month.contains("julho")){
                                        month = "julho";
                                    }else{
                                        if(month.contains("agosto")){
                                            month = "agosto";
                                        }else{
                                            if(month.contains("setembro")){
                                                month = "setembro";
                                            }else{
                                                if(month.contains("outubro")){
                                                    month = "outubro";
                                                }else{
                                                    if(month.contains("novembro")){
                                                        month = "novembro";
                                                    }else{
                                                            month = "dezembro";
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

            return getMonth(month);
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






































