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

import static cf.castellon.turistorre.utils.Utils.*;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

public class MyFireAdapterGaleriaRecyclerView extends FirebaseRecyclerAdapter<Imagen,MyFireAdapterGaleriaRecyclerView.MyFireViewHolder>
        implements View.OnClickListener{
    private Context context;
    private View.OnClickListener listener;

    public MyFireAdapterGaleriaRecyclerView(Class<Imagen> modelClass, int modelLayout, Class<MyFireViewHolder> viewHolderClass, DatabaseReference ref,Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_fire_recycle, viewGroup, false);
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

    //Casos especiales:
    //a)Si otro usuario añade una foto y este usuario la pincha se tendra que actualizar en su bbdd. Por lo tanto la añadimos
    //b)Si hay un usuario nuevo que ha creado una foto añadimos tambien el usuario
    @Override
    protected void populateViewHolder(final MyFireViewHolder viewHolder, final Imagen modelo, int position) {
        if (buscarImagen(modelo.getUidImg())==null)/*a)*/
            imagenes.add(modelo);
        if (!existeUsuario(modelo.getUidUser()))/*b)*/
            anyadirUsuario(modelo.getUidUser());

        viewHolder.bindDatos(modelo.getUriStrPre(),context);
    }

    /** Clase ViewHolder interna */
    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public MyFireViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.ivFilaRecycle);
        }

        public void bindDatos(String urlStr, Context context){
            Uri url = Uri.parse(urlStr);
            Glide
                    .with(context)
                    .load(url)
//                    .centerCrop()
//                    .fitCenter()
                    .placeholder(R.drawable.escudo)
                    .crossFade()
                    .into(imageView);
        }

    }
}