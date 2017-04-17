package cf.castellon.turistorre.fragments.Click;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

public class RaconsPagina extends Fragment {
    Imagen raco;
    VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();
    View inflatedView;
    @BindView(R.id.tvTituloRacons) TextView tvTitulo;
    @BindView(R.id.tvDescripcionRacons) TextView tvDescripcion;
    @BindView(R.id.vrRaco) VrPanoramaView vrPano;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        raco = getArguments().getParcelable("raco");
        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.mipagefragment_carrusel_raco_layout, container, false);

        ButterKnife.bind(this,inflatedView);

        tvTitulo.setText(raco.getTitulo());
        tvDescripcion.setText(raco.getDescripcion());
        Uri urlImg = Uri.parse(raco.getUriStr());
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
