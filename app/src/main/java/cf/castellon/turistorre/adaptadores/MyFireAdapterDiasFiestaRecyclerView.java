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
import cf.castellon.turistorre.bean.DiaFiestaMeta;
import static cf.castellon.turistorre.utils.Utils.*;



public class MyFireAdapterDiasFiestaRecyclerView extends FirebaseIndexRecyclerAdapter<DiaFiestaMeta, MyFireAdapterDiasFiestaRecyclerView.MyFireViewHolder>
        implements View.OnClickListener {
    private View.OnClickListener listener;

    public MyFireAdapterDiasFiestaRecyclerView(Class<DiaFiestaMeta> modelClass, int modelLayout, Class<MyFireViewHolder> viewHolderClass, Query keyRef, DatabaseReference  dataRef) {
        super(modelClass, modelLayout, viewHolderClass, keyRef, dataRef);
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_diasfiesta_layout, viewGroup, false);
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
    protected void populateViewHolder(MyFireViewHolder viewHolder, DiaFiestaMeta modelo, int position) {
        viewHolder.bindDatos(modelo.getTituloDiaFiesta());
    }

    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTituloDiaFiesta) TextView textoTv;

        public MyFireViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindDatos(String titulo) {
            textoTv.setText(titulo);
            cambiarDrawableFondoTV(textoTv);
        }
  }

}
