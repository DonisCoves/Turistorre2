package cf.castellon.turistorre.fragments.Click;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class BandoSeleccionado extends Fragment {
    Imagen bando;
    @BindView(R.id.tvTitSelecBando) TextView tvTitulo;
    @BindView(R.id.tvDescSelecBando) TextView tvDescripcion;
    @BindView(R.id.ivSelecBando) ImageView ivImagen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bando = getArguments().getParcelable("bando");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        Uri urlImg;

        view = inflater.inflate(R.layout.bando_seleccionado_layout,container,false);
        ButterKnife.bind(this, view);
        tvTitulo.setText(bando.getTitulo());
        tvDescripcion.setText(bando.getDescripcion());
        urlImg = Uri.parse(bando.getUriStr());
        Glide
                .with(getContext())
                .load(urlImg)
                .placeholder(R.drawable.escudo)
                .crossFade()
                .into(ivImagen);
        return view;
    }
}
