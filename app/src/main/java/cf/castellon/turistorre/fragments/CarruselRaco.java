package cf.castellon.turistorre.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Raco;
import cf.castellon.turistorre.bean.Usuario;

/**
 * Created by pccc on 25/03/2016.
 */

public class CarruselRaco extends Fragment {
    Raco raco;
    VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();
    View inflatedView;
    TextView tvTitulo,tvDescripcion;
    VrPanoramaView vrPano;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        raco = getArguments().getParcelable("raco");
        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.mipagefragment_carrusel_raco_layout, container, false);
        tvTitulo = (TextView) inflatedView.findViewById(R.id.tvTituloRacons);
        tvDescripcion = (TextView) inflatedView.findViewById(R.id.tvDescripcionRacons);
        vrPano = (VrPanoramaView) inflatedView.findViewById(R.id.vrRaco);

        tvTitulo.setText(raco.getTitulo());
        tvDescripcion.setText(raco.getDescripcion());
        Uri urlImg = Uri.parse(raco.getUrl());
        Glide
                .with(getContext())
                .load(urlImg)
                .asBitmap()
                .placeholder(R.drawable.escudo)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        vrPano.loadImageFromBitmap(resource, panoOptions);
                    }
                });
        return inflatedView;
    }

}
