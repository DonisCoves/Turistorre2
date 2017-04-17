package cf.castellon.turistorre.fragments.Click;

import android.app.Activity;
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

/**
 * Created by pccc on 01/12/2016.
 */

public class BandoSeleccionado extends Fragment {
    @BindView(R.id.tvTitSelecBando) TextView tvTitulo;
    @BindView(R.id.tvDescSelecBando) TextView tvDescripcion;
    @BindView(R.id.ivSelecBando) ImageView ivImagen;
    private Bundle bund;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bund = getArguments();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bando_seleccionado_layout,container,false);
        ButterKnife.bind(this, view);

        tvTitulo.setText(bund.getString("TITULO"));
        tvDescripcion.setText(bund.getString("DESCRIPCION"));
        Uri url = Uri.parse(bund.getString("IMAGEN_URL_STR"));

        Glide
                .with(getContext())
                .load(url)
//                    .centerCrop()
//                    .fitCenter()
                .placeholder(R.drawable.escudo)
                .crossFade()
                .into(ivImagen);
        return view;
    }
}
