package com.example.whereapplication.ActivitiesClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.whereapplication.Adapter.AdapterList;
import com.example.whereapplication.Object.Event;

import com.example.whereapplication.Object.FineLocation;
import com.example.whereapplication.Object.Price;

import com.example.whereapplication.Object.User;
import com.example.whereapplication.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
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


import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import java.util.List;


import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.View.INVISIBLE;
import static com.example.whereapplication.ActivitiesClasses.LoginActivity.filterLocation;
import static com.example.whereapplication.ActivitiesClasses.LoginActivity.filterState;
import static com.example.whereapplication.ActivitiesClasses.LoginActivity.removeAccents;


public class ListActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference dbReference;

    TextView tvTitle;
    TextView tvPrice;
    TextView tvDate;
    TextView tvAddress;
    EditText etSearch;
    Bundle extras;
    String location;
    RecyclerView recyclerView;
    private AdapterList adapterList;
    String filter;
    DatabaseReference saveReference;
    List<Event> eventList;
    ProgressBar progressBar;
    ImageButton btnSearch;
    int i = 0;
    int gridSize;
    Toolbar myToolBar;
    AutoCompleteTextView etLocation;
    private RequestQueue requestQueue;
    User user;
    TextView tvLocationMenu;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navigation, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_list);
        myToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolBar);
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvPrice = findViewById(R.id.tvPrice);
        tvAddress = findViewById(R.id.tvAddress);

//        btnSearch = findViewById(R.id.btnSearch);
//        etLocation = findViewById(R.id.etLocation);
        database = FirebaseDatabase.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        extras = intent.getExtras();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                myToolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        tvLocationMenu = navigationView.getHeaderView(0).findViewById(R.id.textView);
        tvLocationMenu.setPaintFlags(tvLocationMenu.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLocationMenu.setOnClickListener(v -> {
            Places.initialize(getApplicationContext(), "AIzaSyAQAh2e4Q-57fwT0tDXmKdy6U76i7QDfGY");

            PlacesClient placesClient = Places.createClient(this);
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent1 = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent1, AUTOCOMPLETE_REQUEST_CODE);
        });
        DatabaseReference userReference = database.getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                user = User.get(dataSnapshot);
                tvLocationMenu.setText(user.getLocation().getCity() + ", " + user.getLocation().getState());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                user = User.get(dataSnapshot);
                tvLocationMenu.setText(user.getLocation().getCity() + ", " + user.getLocation().getState());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        etLocation.setText(location);

        progressBar = findViewById(R.id.progressLoader);
        recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        eventList = new ArrayList<>();
        etSearch = findViewById(R.id.etSearch);
        this.etSearch.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    filter = etSearch.getText().toString();
                    location = removeAccents(user.getLocation().getCity().toUpperCase()) + "-" + user.getLocation().getState();
                    saveReference = database.getReference("/" + location + "/" + filter);
                    if (!location.isEmpty() && !filter.isEmpty()) {
                        saveReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    new Search().execute();
                                }
                                getEvents();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        new AlertDialog.Builder(ListActivity.this)
                                .setTitle("Busca")
                                .setMessage("Prencha todos os campos de busca")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    etSearch.setText("");
                    return true;
                }

            }
            return false;
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        InputMethodManager in = (InputMethodManager) ListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(etSearch.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        filter = etSearch.getText().toString();
                        location = removeAccents(user.getLocation().getCity().toUpperCase()) + "-" + user.getLocation().getState();

                        saveReference = database.getReference("/" + location + "/" + filter);
                        if (!location.isEmpty() && !filter.isEmpty()) {
                            saveReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        new Search().execute();
                                    }
                                    getEvents();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            new AlertDialog.Builder(ListActivity.this)
                                    .setTitle("Busca")
                                    .setMessage("Prencha todos os campos de busca")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        etSearch.setText("");
                    }
                    return true;
                }
                return false;
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            String country;
            String city;
            String state;
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            User user1 = new User();
           String TAG = "prediction";
            if (resultCode == RESULT_OK) {
                Geocoder geocoder = new Geocoder(this);

                Place place = Autocomplete.getPlaceFromIntent(data);
                try {
                    List<Address> location;
                   location = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 3);
                    country = location.get(0).getCountryCode();
                    city  = location.get(0).getSubAdminArea();
                    state = location.get(0).getAdminArea();
                    Log.i("logtest",country);
                    state = filterState(state);
                    FineLocation fineLocation = new FineLocation();
                    fineLocation.setCity(city);
                    fineLocation.setCountry(country);
                    fineLocation.setState(state);
                    user1.setLocation(fineLocation);
                    dbReference.child(uid).setValue(user1);
                }catch (Exception e ){
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void getEvents() {

        eventList.clear();

        DatabaseReference listenerReference = database.getReference().child(location).child(filter);

        listenerReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                Event e = Event.get(snapshot);
                eventList.add(e);
                adapterList = new AdapterList(eventList);
                recyclerView.setAdapter(adapterList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event e = Event.get(dataSnapshot);
                eventList.add(e);
                adapterList = new AdapterList(eventList);
                recyclerView.setAdapter(adapterList);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public class Search extends AsyncTask<Void, Void, Void> {

        String eventLocation;
        String eventAddress;
        String eventName;
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
        Event[] eventList = new Event[630];
        String checkMin;
        String image;


        @Override
        protected void onPreExecute()
        {
            String city = LoginActivity.removeAccents(user.getLocation().getCity());
           eventLocation = city+"-"+user.getLocation().getState();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public Void doInBackground(Void... voids) {
            for (int p = 0; p < 630; p++) {
                Event e = new Event();
                eventList[p] = e;
            }
            try {
                currentEvent = 0;
                //    Log.i("teste","time: "+Timestamp.parse("2019-04-15 14:00:00"));
                for (j = 1; j < 30; j++) {
                    Log.i("teste_page", "page" + j);
                    url = "https://www.sympla.com.br/eventos/" + eventLocation + "?s=" + filter + "&pagina=" + j;
                    Log.i("teste_main", url);
                    doc = Jsoup.connect(url).get();
                    Elements ifExists = doc.normalise().select("h3[class=pull-left]");
                    if (!ifExists.isEmpty()) {
                        break;
                    }
                    Elements eventGrid = doc.select("li.search-result-li");
                    gridSize = eventGrid.size();
                    Log.i("teste_gridsize", "" + gridSize);
                    cellUrl = new String[gridSize];
                    int cont = currentEvent;
                    for (i = 0; i < gridSize; i++) {
                        Elements singleEvent = doc.select("a.sympla-card").eq(i);
                        image = singleEvent.select("div.event--image").attr("style");
                        if (!image.contains("default")) {
                            image = image.substring(23, 72);
                        }
                        cellUrl[i] = doc.select("a.sympla-card").eq(i).attr("href");
                        Elements eventLocationElement = singleEvent.select("div.event-location");
                        Elements eventNameElement = singleEvent.select("div.event-name").select("span");
                        eventAddress = eventLocationElement.text();
                        eventName = eventNameElement.text();
                        Log.i("teste", eventLocation + " " + eventName + " " + image);
                        if (eventName.contains("&")) {
                            eventName = eventName.replace("&", "And");
                        }
                        if (eventName.contains("@")) {
                            eventName = eventName.replace("@", " ");
                        }
                        if (eventName.contains("/")) {
                            eventName = eventName.replace("/", "-");
                        }
                        if (eventName.contains(".")) {
                            eventName = eventName.replace(".", "-");
                        }
                        eventList[cont].setTitle(eventName);
                        eventList[cont].setLocal(eventAddress);
                        int eventDay = Integer.parseInt(singleEvent.select("div.event-date-day").text());
                        int eventMonth = Integer.parseInt(getMonth(singleEvent.select("div.event-card-date-month").text()));
                        boolean happening = false;
                        Elements hourElement = singleEvent.select("div.event-card-info");
                        hour = Integer.parseInt(hourElement.text().substring(0, 2));
                        if (hourElement.text().contains("andamento")) {
                            happening = true;
                            hour = Calendar.getInstance().get(Calendar.HOUR);
                        }
                        int sec = 0;
                        min = Integer.parseInt(hourElement.text().substring(3, 5));
                        if (happening) {
                            min = Calendar.getInstance().get(Calendar.MINUTE);
                        }
                        Log.i("teste_hora", "" + eventDay + " " + eventMonth + " " + hour + " " + min + " " + sec);
                        Calendar date = Calendar.getInstance();
                        date.set(date.get(Calendar.YEAR), eventMonth, eventDay, hour, min);
                        dateStamp = new Timestamp(date.getTime().getTime());
                        Log.i("teste", date.getTime() + "");
                        eventList[cont].setPhoto(image);
                        Log.i("imagem", image);
                        eventList[cont].setDate(dateStamp);
                        cont++;
                    }
                    for (String cellEvent : cellUrl) {
                        Log.i("teste_url", cellEvent);
                        try {
                            eventCell = Jsoup.connect(cellEvent).get();
                            Elements eventPriceSizeElement = eventCell.select("form#ticket-form").select("tr");
                            eventPriceSize = eventPriceSizeElement.size();
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                        Price[] priceObj = new Price[eventPriceSize];
                        for (int p = 0; p < priceObj.length; p++) {
                            Price price = new Price();
                            priceObj[p] = price;
                        }
                        for (p = 1; p < eventPriceSize; p++) {
                            Log.i("teste_p", eventPriceSize + "");
                            double eventPriceNum;
                            Elements eventPriceCheck = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td.opt-panel");

                            if (eventPriceCheck.text().contains("Esgotado") || eventPriceCheck.text().contains("Encerrado") || eventPriceCheck.text().contains("Não iniciado")) {
                                Log.i("teste_p", p + "esgotado");
                                priceObj[p - 1].setValue(0);
                                priceObj[p - 1].setLote("Nao disponivel");

                            } else {
                                Elements eventPrice = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td").eq(0);
                                String free = eventPrice.text().toLowerCase();
                                free = LoginActivity.removeAccents(free);
//                                        Log.i("teste",free);
                                if (free.contains("gratis")) {
                                    eventPriceNum = 0;
                                    priceObj[p - 1].setValue(eventPriceNum);
                                    Elements eventPriceDoc1 = eventPrice.select("span").eq(0);
                                    priceObj[p - 1].setLote(eventPriceDoc1.text());
//                                    List prices = Arrays.asList(priceObj);
//                                    eventList[currentEvent].setPrice(prices);
                                    Log.i("teste_lote gratis", "" + priceObj[p].getLote() + " " + priceObj[p].getValue());
                                } else {
                                    Log.i("teste_p", p + "n esgotado");
                                    Elements checkID = eventCell.normalise().select("form#ticket-form").select("tr").eq(p);
                                    if (checkID.attr("id").equals("show-discount") || checkID.attr("id").equals("discount-form")) {
                                        break;
                                    } else {
                                        Elements eventPriceExists = checkID.select("td").eq(0).select("span").eq(1);
                                        String price = eventPriceExists.text().substring(3);
                                        price = price.replaceAll(",", ".");
                                        if (price.indexOf(".") < price.lastIndexOf(".")) {
                                            price = price.substring(0, price.lastIndexOf("."));
                                        }
                                        Log.i("teste", "" + price);
                                        eventPriceNum = Double.valueOf(price);
                                        priceObj[p - 1].setValue(eventPriceNum);
                                        Elements eventPriceDoc1 = eventCell.normalise().select("form#ticket-form").select("tr").eq(p).select("td").eq(0).select("span").eq(0);
                                        String nome = eventPriceDoc1.text();
                                        priceObj[p - 1].setLote(nome);
//                                        List prices = Arrays.asList(priceObj);
//                                        eventList[currentEvent].setPrice(prices);
                                        Log.i("teste_LOte e Preco", "" + priceObj[p - 1].getLote() + " " + priceObj[p - 1].getValue());
                                    }
                                }
                            }
                            List prices = Arrays.asList(priceObj);
                            eventList[currentEvent].setPrice(prices);
                            Log.i("teste", priceObj[p - 1].getLote());
                        }
                        Log.i("teste_evento", "" + currentEvent);
                        eventList[currentEvent].setTitle(eventList[currentEvent].getTitle().replace("#", ""));
                        Log.i("teste_titulo", eventList[currentEvent].getTitle());
                        Log.i("teste_data", eventList[currentEvent].getDate() + "");
                        Log.i("teste_local", eventList[currentEvent].getLocal());
                        dbReference.child(eventLocation.toUpperCase()).child(filter).child(eventList[currentEvent].getTitle()).setValue(eventList[currentEvent]);
                        currentEvent++;
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(INVISIBLE);
        }
    }

    private String getMonth(String month) {
        switch (month) {
            case "Jan":
                month = "1";
                break;
            case "Feb":
                month = "2";
                break;
            case "Mar":
                month = "3";
                break;
            case "Abr":
                month = "4";
                break;
            case "Mai":
                month = "5";
                break;
            case "Jun":
                month = "6";
                break;
            case "Jul":
                month = "7";
                break;
            case "Ago":
                month = "8";
                break;
            case "Set":
                month = "9";
                break;
            case "Out":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dez":
                month = "12";

        }
        return month;
    }

    private String getMonthAgain(String month) {
        if (month.contains("janeiro")) {
            month = "janeiro";

        } else {
            if (month.contains("fevereiro")) {
                month = "fevereiro";
            } else {
                if (month.contains("março")) {
                    month = "março";
                } else {
                    if (month.contains("abril")) {
                        month = "abril";
                    } else {
                        if (month.contains("maio")) {
                            month = "maio";
                        } else {
                            if (month.contains("junho")) {
                                month = "junho";
                            } else {
                                if (month.contains("julho")) {
                                    month = "julho";
                                } else {
                                    if (month.contains("agosto")) {
                                        month = "agosto";
                                    } else {
                                        if (month.contains("setembro")) {
                                            month = "setembro";
                                        } else {
                                            if (month.contains("outubro")) {
                                                month = "outubro";
                                            } else {
                                                if (month.contains("novembro")) {
                                                    month = "novembro";
                                                } else {
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

//    private void jsonParse() {
//                String url = "https://api.myjson.com/bins/e13nh";
//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//                                    JSONArray jsonArray = response.getJSONArray("estados");
//
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        JSONObject estado = jsonArray.getJSONObject(i);
//                                        Log.i("JSONTESTEARRAY",estado.toString());
//                                        String sigla = estado.getString("sigla");
//                                        JSONArray json = estado.getJSONArray("cidades");
//                                        for (int j = 0; j < json.length(); j++) {
//                                            String cidade = json.getJSONObject();
//
//
//
//                                            Log.i("JSONTESTE", cidade + ", " + sigla);
//                                            listCities.add(cidade + ", " + sigla);
//                                        }
//
//
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                });
//
//                requestQueue.add(request);
//
//
//
//
//    }
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






































