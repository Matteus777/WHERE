package com.example.whereapplication.Object;

import android.media.Image;
import android.os.health.TimerStat;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Event {
    private String local;
    private String id;
    private List<Price> price;
    private String title;
    private String photo;
    private Timestamp date;
    private String desc;
    private Calendar realDate;
    public Event(){

    }
    public Event(String local,String id,List<Price> price,String title,String photo,Timestamp date,String desc){
        this.setLocal(local);
        this.setId(id);
        this.setPrice(price);
        this.setTitle(title);
        this.setPhoto(photo);
        this.setDate(date);
        this.setDesc(desc);
                    }


     public static Event get(DataSnapshot snapshot){
        Event event = new Event();
        event.setTitle((String) snapshot.child("title").getValue());
        Timestamp s = new Timestamp((long)snapshot.child("date").child("time").getValue());
        Calendar date = Calendar.getInstance();
        date.setTime(s);
        event.setRealDate( date );
         List<Price> listPrice = new ArrayList<>();
        for( DataSnapshot snap:snapshot.child( "price" ).getChildren()){
            Price p = new Price();
            Object objLote = snap.child ( "lote" ).getValue();
            String  lote = String.valueOf(objLote);

            if( lote.isEmpty()|| lote.equals ( "null" ) ){

                break;

            }

            Object objValue = snap.child( "value" ).getValue();
            double value = Double.parseDouble(objValue.toString());
            p.setValue( value );
            p.setLote( lote );
            listPrice.add( p );
        }

        event.setPrice(listPrice);
        event.setLocal((String)snapshot.child("local").getValue());
        return event;
     }
    public List getPrice() {
        return price;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }

    public Calendar getRealDate() {
        return realDate;
    }

    public void setRealDate(Calendar realDate) {
        this.realDate = realDate;
    }
}
