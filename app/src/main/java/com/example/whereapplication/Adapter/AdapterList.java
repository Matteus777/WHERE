
package com.example.whereapplication.Adapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whereapplication.Object.Event;
import com.example.whereapplication.Object.Price;
import com.example.whereapplication.R;

import java.util.List;

public  class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolderEvent> {
    private List<Event> list;

    public AdapterList(List<Event> dados){
        this.list = dados;
    }





    @NonNull
    @Override
    public AdapterList.ViewHolderEvent onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_adpter,parent,false);

        ViewHolderEvent holderEvent = new ViewHolderEvent(view);
        return holderEvent;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterList.ViewHolderEvent viewHolder, int i) {
        if(list!=null&&list.size()>0) {
            Log.i("cgnotas",i+"");
            Event event = list.get(i);
            viewHolder.tvTitle.setText(event.getTitle());
            viewHolder.tvDate.setText(event.getRealDate().getTime().toString());
            viewHolder.tvLocation.setText(event.getLocal());
            double menor = 99999999;
            for(int j = 0;j<event.getPrice().size();j++){
                Object price = event.getPrice().get(j);
                Price p = (Price) price;
                if(p.getLote().equals("Nao disponivel")){
                    continue;
                }

                if(p.getValue()<menor){
                    viewHolder.tvPrice.setText("R$ "+String.valueOf(p.getValue()));
                    viewHolder.tvLote.setText(p.getLote());
                }
                  menor = p.getValue();

            }
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderEvent extends RecyclerView.ViewHolder{

        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvLocation;
        public TextView tvPrice;
        public TextView tvDate;
        public TextView tvLote;


        public ViewHolderEvent(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLote = itemView.findViewById(R.id.tvLote);


        }



    }
}

