package cf.castellon.turistorre.fragments.Click;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

public class RaconsPagina extends Fragment implements View.OnLongClickListener{
    Imagen raco;
    View inflatedView;
    private View.OnLongClickListener listenerLong;
    @BindView(R.id.tvTituloRacons) TextView tvTitulo;
    @BindView(R.id.tvDescripcionRacons) TextView tvDescripcion;
    @BindView(R.id.ivRaco) ImageView ivPano;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        raco = getArguments().getParcelable("raco");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.mipagefragment_carrusel_raco_layout, container, false);
        ButterKnife.bind(this,inflatedView);
        inflatedView.setOnLongClickListener(this);
        tvTitulo.setText(raco.getTitulo());
        tvDescripcion.setText(raco.getDescripcion());
        Uri urlImg = Uri.parse(raco.getUriStr());
        Glide
                .with(getContext())
                .load(urlImg)
                .asBitmap()
                .thumbnail(0.1f)
                .into(ivPano);
        return inflatedView;
    }

    @Override
    public boolean onLongClick(View v) {
        if(listenerLong != null)
            listenerLong.onLongClick(v);
        return true;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.listenerLong = listener;
    }
}
