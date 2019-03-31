
package com.example.whereapplication.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import com.example.whereapplication.R;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.whereapplication.Object.Event;

import java.util.List;
public class AdpterList extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_adpter);
    }
}
public  class list_Adapter extends BaseAdapter {

    private final List<Event> events;
    private final Activity act;


    public list_Adapter (List<CalendarContract.Events> eventos, Activity act) {
        this.eventos = enventos;
        this.act = act;

    }

    @Override
    public int getCount() {
        return eventos.size();
    }

    @Override
    public Object getItem(int position) {
        return eventos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater()
                .inflate(R.layout.activity_list, parent, false);
        Even eventos = eventos.get(position);
        return null;

        //pegando as referências das Views
        TextView titulo = (TextView)
                view.findViewById(R.id.list_adpter_title);
        TextView local = (TextView)
                view.findViewById(R.id.list_adpter_localization);
        TextView data = (TextView)
                view.findViewById(R.id.list_adpter_date);
        TextView preco = (TextView)
                view.findViewById(R.id.list_adpter_price);
        ImageView imagem = (ImageView)
                view.findViewById(R.id.list_adpter_image);


        //populando as Views
        titulo.setText(eventos.getTitulo());
        preco.setText(eventos.getPreco());
        data.setText(eventos.getData());
        local.setText(eventos.getLocal());
        imagem.setImageResource(R.drawable.java);

        return view;
    }
}


