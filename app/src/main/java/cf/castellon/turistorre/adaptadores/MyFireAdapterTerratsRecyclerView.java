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
import cf.castellon.turistorre.bean.Panoramica;

import static cf.castellon.turistorre.utils.Utils.*;

public class MyFireAdapterTerratsRecyclerView extends FirebaseRecyclerAdapter<Panoramica,MyFireAdapterTerratsRecyclerView.MyFireViewHolder>
                                              implements View.OnClickListener{
    private Context context;
    private View.OnClickListener listener;

    public MyFireAdapterTerratsRecyclerView(Class<Panoramica> modelClass, int modelLayout, Class<MyFireViewHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_fire_terrat_recycle, viewGroup, false);
        itemView.setOnClickListener(this);
        MyFireViewHolder holder = new MyFireViewHolder(itemView);
        return holder;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    @Override
    protected void populateViewHolder(final MyFireViewHolder viewHolder, final Panoramica modelo, int position) {
        if (buscarPanoramica(modelo.getUidImg())==null)
            panoramicas.add(modelo);
        viewHolder.bindDatos(modelo.getUriStrPre(),context);
    }

    /** Clase ViewHolder interna */
    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public MyFireViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.ivFilaRecycleTerrat);
        }

        public void bindDatos(String urlStr, Context context){
            Uri url = Uri.parse(urlStr);
            Glide
                    .with(context)
                    .load(url)
                    .placeholder(R.drawable.escudo)
                    .crossFade()
                    .into(imageView);
        }

    }
}