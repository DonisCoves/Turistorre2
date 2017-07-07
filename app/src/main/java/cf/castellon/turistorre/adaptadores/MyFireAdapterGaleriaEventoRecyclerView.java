package cf.castellon.turistorre.adaptadores;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import static cf.castellon.turistorre.utils.Utils.*;

public class MyFireAdapterGaleriaEventoRecyclerView extends FirebaseRecyclerAdapter<Imagen,MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder>
        implements View.OnClickListener, View.OnLongClickListener{
    private View.OnClickListener listener;
    private String uidDiaFiesta;
    private String uidEvento;
    private View.OnLongClickListener listenerLong;

    public MyFireAdapterGaleriaEventoRecyclerView(Class<Imagen> modelClass, int modelLayout,
                                                  Class<MyFireViewHolder> viewHolderClass, DatabaseReference ref,
                                                  String uidDiaFiesta, String uidEvento  ) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.uidDiaFiesta = uidDiaFiesta;
        this.uidEvento = uidEvento;
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_eventos_galeria, viewGroup, false);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        return  new MyFireViewHolder(itemView);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.listenerLong = listener;
    }

    @Override
    protected void populateViewHolder(MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder viewHolder, Imagen model, int position) {
        viewHolder.bindDatos(model.getUriStr());
       anyadirImagenDiaFiesta(model,uidDiaFiesta,uidEvento);
    }

    @Override
    public boolean onLongClick(View view) {
        if(listenerLong != null)
            listenerLong.onLongClick(view);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivEvGaleria) ImageView imageView;
        public MyFireViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void bindDatos(String urlStr){
            Uri url = Uri.parse(urlStr);
            Glide
                    .with(imageView.getContext())
                    .load(url)
                    .thumbnail(0.1f)
                    .crossFade()
                    .into(imageView);
        }

    }
}