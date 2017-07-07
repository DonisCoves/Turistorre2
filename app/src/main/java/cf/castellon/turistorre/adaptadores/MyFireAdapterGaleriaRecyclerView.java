package cf.castellon.turistorre.adaptadores;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import static cf.castellon.turistorre.utils.Utils.*;

@SuppressWarnings("unchecked")
public class MyFireAdapterGaleriaRecyclerView extends FirebaseIndexRecyclerAdapter<Imagen,MyFireAdapterGaleriaRecyclerView.MyFireViewHolder>
        implements View.OnClickListener, View.OnLongClickListener{
    private View.OnClickListener listener;
    private View.OnLongClickListener listenerLong;

    public MyFireAdapterGaleriaRecyclerView(Class<Imagen> modelClass, int modelLayout, Class<MyFireViewHolder> viewHolderClass, Query keyRef, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass,keyRef, ref);
    }

    @Override
    public MyFireViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_fire_recycle, viewGroup, false);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        return (new MyFireViewHolder(itemView));
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }


    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.listenerLong = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    @Override
    public boolean onLongClick(View view) {
        if(listenerLong != null)
            listenerLong.onLongClick(view);
        return true;
    }

     @Override
    protected void populateViewHolder(final MyFireViewHolder viewHolder, final Imagen modelo, int position) {
        viewHolder.bindDatos(modelo.getUriStr());
    }

    public static class MyFireViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivFilaRecycle) ImageView imageView;

        private MyFireViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindDatos(String urlStr){
            Uri url = Uri.parse(urlStr);
            Glide
                    .with(imageView.getContext())
                    .load(url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .into(imageView);
        }

    }
}