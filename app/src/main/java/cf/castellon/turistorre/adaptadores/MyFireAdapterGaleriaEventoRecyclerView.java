package cf.castellon.turistorre.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

/**
 * Created by pccc on 02/03/2017.
 */
public class MyFireAdapterGaleriaEventoRecyclerView extends FirebaseRecyclerAdapter<Imagen,MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder>
        implements View.OnClickListener {
    private View.OnClickListener listener;

    public MyFireAdapterGaleriaEventoRecyclerView(Class<Imagen> modelClass, int modelLayout, Class<MyFireViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_eventos_galeria, viewGroup, false);
        itemView.setOnClickListener(this);
        MyFireViewHolder holder = new MyFireViewHolder(itemView);
        return holder;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder viewHolder, Imagen model, int position) {
        viewHolder.bindDatos(model.getUriStrPre());
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    /** Clase ViewHolder interna */
    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public MyFireViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.ivEvGaleria);
        }

        public void bindDatos(String urlStr){
            Uri url = Uri.parse(urlStr);
            Glide
                    .with(imageView.getContext())
                    .load(url)
//                    .centerCrop()
//                    .fitCenter()
                    .placeholder(R.drawable.escudo)
                    .crossFade()
                    .into(imageView);
        }

    }
}