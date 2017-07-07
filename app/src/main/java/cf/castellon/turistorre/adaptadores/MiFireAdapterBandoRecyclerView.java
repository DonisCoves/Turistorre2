package cf.castellon.turistorre.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

import static cf.castellon.turistorre.utils.Utils.*;

public class MiFireAdapterBandoRecyclerView extends FirebaseIndexRecyclerAdapter<Imagen, MiFireAdapterBandoRecyclerView.MiFireViewHolder>
                                            implements View.OnClickListener {
    private View.OnClickListener listener;

    public MiFireAdapterBandoRecyclerView(Class<Imagen> modelClass, int modelLayout, Class <MiFireViewHolder>viewHolderClass, Query keyRef, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, keyRef, ref);
    }

    @Override
    public MiFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_bando_layout, viewGroup, false);
        cambiarColor(itemView.getContext(),itemView);
        itemView.setOnClickListener(this);
        return (new MiFireViewHolder(itemView));
    }

//    @Override
//    public Imagen getItem(int position) {
//        return super.getItem(getItemCount() - position - 1);
//    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    @Override
    protected void populateViewHolder(MiFireViewHolder viewHolder, Imagen model, int position) {
        viewHolder.bindDatos(model.getTitulo(),model.getFecha());
    }

    public static class MiFireViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTituloBando) TextView tvTitulo;
        @BindView(R.id.tvFecha) TextView tvFecha;

        private MiFireViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindDatos(String titulo, String fecha){
            tvTitulo.setText(titulo);
            tvFecha.setText(fecha);
        }
    }
}