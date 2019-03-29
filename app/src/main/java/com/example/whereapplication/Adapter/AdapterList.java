//package com.example.whereapplication.Adapter;
//
//import android.app.Activity;
//import android.widget.BaseAdapter;
//
//import com.example.whereapplication.Object.Event;
//
//import java.util.List;
//
//public class AdapterList extends BaseAdapter {
//
//
//    private final List<Event> events;
//    private final Activity activity;
//
//    public AdapterList(Activity activity, List<Event> events) {
//        this.events = events;
//        this.activity = activity;
//    }
//
//    @Override
//    public int getCount() {
//        return this.events.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return this.events.get(position);
//    }
//
//    @Override
//    public int getItemId(int position) {
//        return this.events.get(position).getId();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
////        View linha = convertView;
////        Contato contato = events.get(position);
////        Bitmap bm;
////
////        if(linha == null){
////            linha = this.activity.getLayoutInflater().inflate(R.layout.celula_layout, parent, false);
////        }
//
//        TextView nome = (TextView) linha.findViewById(R.id.nomeCelula);
//        TextView email = (TextView) linha.findViewById(R.id.emailCelula);
//        TextView site = (TextView) linha.findViewById(R.id.siteCelula);
//        TextView telefone = (TextView) linha.findViewById(R.id.telefoneCelula);
//        TextView endereco = (TextView) linha.findViewById(R.id.enderecoCelula);
//
//        if(position%2 == 0){
//            linha.setBackgroundColor(activity.getResources().getColor(R.color.corPar));
//        }else {
//            linha.setBackgroundColor(activity.getResources().getColor(R.color.corImpar));
//        }
//
//        nome.setText(contato.getNome());
//
//        if(contato.getFoto() != null){
//            bm = BitmapFactory.decodeFile(contato.getFoto());
//        }else{
//            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_no_image);
//        }
//        bm = Bitmap.createScaledBitmap(bm, 180, 120, true);
//        ImageView foto = (ImageView) linha.findViewById(R.id.imagemCelula);
//        foto.setImageBitmap(bm);
//
//        if(email != null){email.setText(contato.getEmail());}
//        if(site != null){site.setText(contato.getSite());}
//        if(telefone != null){telefone.setText(contato.getTelefone());}
//        if(endereco != null){endereco.setText(contato.getEndereco());}
//
//        return linha;
//    }
//}
