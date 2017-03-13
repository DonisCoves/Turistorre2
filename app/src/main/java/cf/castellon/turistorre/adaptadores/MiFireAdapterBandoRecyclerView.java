package cf.castellon.turistorre.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Bando;

import static cf.castellon.turistorre.utils.Utils.cambiarColor;

/**
 * Created by pccc on 25/11/2016.
 */

public class MiFireAdapterBandoRecyclerView extends FirebaseRecyclerAdapter<Bando, MiFireAdapterBandoRecyclerView.MiFireViewHolder>
                                            implements View.OnClickListener {
    private Context context;
    private View.OnClickListener listener;

    public MiFireAdapterBandoRecyclerView(Class<Bando> modelClass, int modelLayout, Class <MiFireViewHolder>viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }


    @Override
    public MiFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_bando_layout, viewGroup, false);
        cambiarColor(context,itemView);
        itemView.setOnClickListener(this);
        MiFireViewHolder holder = new MiFireViewHolder(itemView);
        return holder;
    }

    @Override
    public Bando getItem(int position) {
        return super.getItem(getItemCount() - position - 1);
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
    protected void populateViewHolder(MiFireViewHolder viewHolder, Bando model, int position) {
        viewHolder.bindDatos(model.getTitulo(),model.getFecha(),context);
    }

    public static class MiFireViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitulo;
        private TextView tvFecha;

        public MiFireViewHolder(View itemView) {
            super(itemView);
            tvTitulo = (TextView) itemView.findViewById(R.id.tvTituloBando);
            tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
        }

        public void bindDatos(String titulo, String fecha, Context context){
            tvTitulo.setText(titulo);
            tvFecha.setText(fecha);
        }
    }
}
