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
import cf.castellon.turistorre.bean.Evento;
import static cf.castellon.turistorre.utils.Utils.*;

public class MyFireAdapterEventosRecyclerView  extends FirebaseIndexRecyclerAdapter<Evento, MyFireAdapterEventosRecyclerView.MyFireViewHolder>
        implements View.OnClickListener {
    private View.OnClickListener listener;

    public MyFireAdapterEventosRecyclerView(Class<Evento> modelClass, int modelLayout, Class<MyFireViewHolder> viewHolderClass, Query keyRef, DatabaseReference dataRef) {
        super(modelClass, modelLayout, viewHolderClass, keyRef, dataRef);
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_eventos_layout, viewGroup, false);
        cambiarDrawableFondoTV(itemView);

        itemView.setOnClickListener(this);
        return (new MyFireViewHolder(itemView));
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    @Override
    protected void populateViewHolder(MyFireViewHolder viewHolder, Evento model, int position) {
        viewHolder.bindDatos(model);
    }

    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTituloEvento) TextView horaTv;
        @BindView(R.id.tvHora) TextView tituloTv;
        @BindView(R.id.tvDescripcion) TextView descripcionTv;

        public MyFireViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindDatos(Evento evento) {
            tituloTv.setText(evento.getTitulo());
            horaTv.setText(evento.getHora_inicial());
            descripcionTv.setText(evento.getDescripcion());
        }

    }

}
