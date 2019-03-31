//
//package com.example.whereapplication.Adapter;
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import com.example.whereapplication.R;
//import android.provider.CalendarContract;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.whereapplication.Object.Event;
//import java.util.List;
//
//public  class AdapterList extends BaseAdapter {
//    private final List<Event> events;
//    private final Activity act;
//
//    private TextView local;
//    private TextView preco;
//    private TextView titulo;
//    private TextView data;
//    private ImageView imagem;
//    public AdapterList (List<Event> events, Activity act) {
//        this.events = events;
//        this.act = act;
//
//    }
//
//    @Override
//    public int getCount() {
//        return events.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return events.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = act.getLayoutInflater().inflate(R.layout.list_adpter,parent,false);
//        Event event = events.get(position);
//        titulo = view.findViewById(R.id.list_adpter_title);
//        data = view.findViewById(R.id.list_adpter_date);
//        preco = view.findViewById(R.id.list_adpter_price);
//        local = view.findViewById(R.id.list_adpter_localization);
//        imagem = view.findViewById(R.id.list_adpter_image);
//
//
//        //populando as Views
//        titulo.setText(event.getTitle());
//        preco.setText(String.valueOf(event.getPrice()));
//        data.setText(event.getDate());
//        local.setText(event.getLocal());
//        imagem.setImageBitmap(R.drawable.java);
//        return view;
//    }
//}
//
