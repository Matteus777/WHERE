package com.example.whereapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.whereapplication.Adapter.AdapterList;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.whereapplication.R.id.progressLoader;


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
    RecyclerView recyclerView;
    private AdapterList adapterList;
    String filter;
    DatabaseReference saveReference;
    List<Event> eventList;
    ProgressBar progressBar;
    Handler handler = new Handler();
    int i = 0;
    int gridSize;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = "teatro";



        setContentView(R.layout.activity_list);
        database = FirebaseDatabase.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        extras = intent.getExtras();
        location = extras.getString("location");
        saveReference = database.getReference("/" + location + "/" + filter);
        progressBar = findViewById(R.id.progressLoader);

        recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        eventList = new ArrayList<>();
        saveReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    new Search().execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        }

   public void getEvents(){
            DatabaseReference listenerReference = database.getReference().child(location).child(filter);
            listenerReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Event e = Event.get(snapshot);
                        eventList.add(e);
                        adapterList = new AdapterList(eventList);
                        recyclerView.setAdapter(adapterList);

//
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    public class Search extends AsyncTask<Void, Void, Void> {

        String eventLocation;
        String eventName;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String eventDate;
        String[] cellUrl;
        int eventPriceSize;
        Document doc;
        int j;
        Elements eventGrid;
        int p;
        Document eventCell;
        public TextView tvFilter;
        //      public String cellEvent;
        int eventCount;
        int i;
        int hour;
        int min;
        Timestamp dateStamp;
        String url;
        private DatabaseReference firebaseDatabase;
        int currentEvent;
        Event[]eventList  = new Event[630];
        String checkMin;
        String image;


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public Void doInBackground(Void... voids) {
            for(int p = 0;p<630;p++){
               Event e = new Event();
                eventList[p]=e;
            }
            try {
                currentEvent=0;
            //    Log.i("teste","time: "+Timestamp.parse("2019-04-15 14:00:00"));
                for (j = 1; j < 30; j++) {
                    Log.i("teste_page","page"+j);
                    url = "https://www.sympla.com.br/eventos/"+location+"?s="+filter+"&pagina="+j;
                    Log.i("teste_main",url);
                    doc = Jsoup.connect(url).get();
                    Elements ifExists = doc.normalise().select("h3[class=pull-left]");
                    if (!ifExists.isEmpty()) {
                        break;
                    }
                    Elements eventGrid = doc.select("li.search-result-li");
                    gridSize = eventGrid.size();
                    Log.i("teste_gridsize",""+gridSize);
                    cellUrl = new String[gridSize];
                    int cont = currentEvent;
                    for ( i = 0; i < gridSize; i++) {
                        Elements singleEvent = doc.select("a.sympla-card").eq(i);
                        image = singleEvent.select("div.event--image").attr("style");
                        if(!image.contains("default")){
                            image = image.substring(23,72);
                        }
                        cellUrl[i] = doc.select("a.sympla-card").eq(i).attr("href");
                        Elements eventLocationElement = singleEvent.select("div.event-location");
                        Elements eventNameElement = singleEvent.select("div.event-name").select("span");
                        eventLocation = eventLocationElement.text();
                        eventName = eventNameElement.text();
                        Log.i("teste",eventLocation+" "+eventName+ " "+image);
                        if (eventName.contains("&")) {
                            eventName = eventName.replace("&", "And");
                        }
                        if (eventName.contains("@")) {
                            eventName = eventName.replace("@"," ");
                        }
                        if(eventName.contains("/")){
                            eventName = eventName.replace("/","-");
                        }
                        if(eventName.contains(".")){
                            eventName = eventName.replace(".","-");
                        }
                        eventList[cont].setTitle(eventName);
                        eventList[cont].setLocal(eventLocation);
                        int eventDay = Integer.parseInt(singleEvent.select("div.event-date-day").text());
                        int eventMonth = Integer.parseInt(getMonth(singleEvent.select("div.event-card-date-month").text()));
                        boolean happening = false;
                        Elements hourElement = singleEvent.select("div.event-card-info");
                        hour = Integer.parseInt(hourElement.text().substring(0, 2));
                        if(hourElement.text().contains("andamento")){
                            happening = true;
                            hour = Calendar.getInstance().get(Calendar.HOUR);
                        }
                        int sec = 0;
                            min = Integer.parseInt(hourElement.text().substring(3,5));
                            if(happening) {
                                min = Calendar.getInstance().get(Calendar.MINUTE);
                            }
                            Log.i("teste_hora",""+ eventDay+" "+eventMonth+" "+hour+" "+min+" "+ sec);
                        Calendar date = Calendar.getInstance();
                        date.set(date.get(Calendar.YEAR), eventMonth,eventDay,hour,min);
                        dateStamp = new Timestamp(date.getTime().getTime());
                        Log.i("teste",date.getTime()+"");
                        eventList[cont].setPhoto(image);
                        eventList[cont].setDate(dateStamp);
                        cont++;
                    }
                    for(String cellEvent:cellUrl){
                        Log.i("teste_url",cellEvent);
                        try {
                            eventCell = Jsoup.connect(cellEvent).get();
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
                            Log.i("teste_p",eventPriceSize+"");
                            double eventPriceNum;
                            Elements eventPriceCheck = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td.opt-panel");

                            if(eventPriceCheck.text().contains("Esgotado")||eventPriceCheck.text().contains("Encerrado")||eventPriceCheck.text().contains("Não iniciado")){
                                Log.i("teste_p",p+"esgotado");
                                priceObj[p-1].setValue(0);
                            priceObj[p-1].setLote("Nao disponivel");

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
                                    priceObj[p-1].setLote(eventPriceDoc1.text());
//                                    List prices = Arrays.asList(priceObj);
//                                    eventList[currentEvent].setPrice(prices);
                                    Log.i("teste_lote gratis",""+priceObj[p].getLote()+" "+priceObj[p].getValue());
                                }
                                else {
                                    Log.i("teste_p",p+"n esgotado");
                                    Elements checkID = eventCell.normalise().select("form#ticket-form").select("tr").eq(p);
                                    if(checkID.attr("id").equals("show-discount")||checkID.attr("id").equals("discount-form")){
                                        break;
                                    }else{
                                        Elements eventPriceExists = checkID.select("td").eq(0).select("span").eq(1);
                                        String price = eventPriceExists.text().substring(3);
                                        price = price.replaceAll(",", ".");
                                        if(price.indexOf(".")<price.lastIndexOf(".")){
                                            price = price.substring(0,price.lastIndexOf("."));
                                        }
                                        Log.i("teste",""+price);
                                        eventPriceNum = Double.valueOf(price);
                                        priceObj[p-1].setValue(eventPriceNum);
                                        Elements eventPriceDoc1 = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td").eq(0).select("span").eq(0);
                                        String nome = eventPriceDoc1.text();
                                        priceObj[p-1].setLote(nome);
//                                        List prices = Arrays.asList(priceObj);
//                                        eventList[currentEvent].setPrice(prices);
                                        Log.i("teste_LOte e Preco",""+priceObj[p-1].getLote()+" "+priceObj[p-1].getValue());
                                    }
                                }
                            }
                            List prices = Arrays.asList(priceObj);
                            eventList[currentEvent].setPrice(prices);
                            Log.i("teste",priceObj[p-1].getLote());
                        }
                        Log.i("teste_evento",""+currentEvent);
                        eventList[currentEvent].setTitle(eventList[currentEvent].getTitle().replace("#",""));
                        Log.i("teste_titulo",eventList[currentEvent].getTitle());
                        Log.i("teste_data",eventList[currentEvent].getDate()+"");
                        Log.i("teste_local",eventList[currentEvent].getLocal());
                        dbReference.child(location).child(filter).child(eventList[currentEvent].getTitle()).setValue(eventList[currentEvent]);
                        currentEvent++;
                    }
                }
                getEvents();
                progressBar.setVisibility(INVISIBLE);
            }catch (Exception e) {
                e.getStackTrace();
            }
            return null; }
                        }

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






































