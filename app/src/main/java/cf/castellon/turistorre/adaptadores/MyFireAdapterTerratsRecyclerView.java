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

import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.utils.Constantes;

import static cf.castellon.turistorre.utils.Utils.*;

@SuppressWarnings("unchecked")
public class MyFireAdapterTerratsRecyclerView extends FirebaseRecyclerAdapter<Imagen,MyFireAdapterTerratsRecyclerView.MyFireViewHolder>
                                              implements View.OnClickListener{
    private View.OnClickListener listener;

    public MyFireAdapterTerratsRecyclerView(Class<Imagen> modelClass, int modelLayout, Class<MyFireViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_fire_terrat_recycle, viewGroup, false);
        itemView.setOnClickListener(this);
        return (new MyFireViewHolder(itemView));
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
    protected void populateViewHolder(final MyFireViewHolder viewHolder, final Imagen modelo, int position) {
        HashSet<Imagen> terrats;
        HashSet<Usuario> usuarios;
        Usuario usuario;

        usuario = buscarUsuario(modelo.getUidUser());
        terrats = baseDatos.get(Constantes.Tablas.Terrats.name());
        usuarios = baseDatos.get(Constantes.Tablas.Usuarios.name());
        terrats.add(modelo);
        usuarios.add(usuario);
        baseDatos.put(Constantes.Tablas.Terrats.name(),terrats);
        baseDatos.put(Constantes.Tablas.Usuarios.name(),usuarios);
        viewHolder.bindDatos(modelo.getUriStrPre());
    }

    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivFilaRecycleTerrat) ImageView imageView;

        public MyFireViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void bindDatos(String urlStr){
            Uri url = Uri.parse(urlStr);
            Glide
                    .with(itemView.getContext())
                    .load(url)
                    .placeholder(R.drawable.escudo)
                    .crossFade()
                    .into(imageView);
        }
    }
}