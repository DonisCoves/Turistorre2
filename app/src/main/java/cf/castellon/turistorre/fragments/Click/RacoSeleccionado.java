package cf.castellon.turistorre.fragments.Click;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

import static cf.castellon.turistorre.utils.Utils.*;

public class RacoSeleccionado extends Fragment  {
    Imagen pano;
    private VrPanoramaView.Options panoOptions;
    @BindView(R.id.vrRaco) VrPanoramaView panoWidgetView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean hayGiroscopio;
        PackageManager packageManager;

        super.onCreate(savedInstanceState);
        pano = getArguments().getParcelable("imagen");
        panoOptions = new VrPanoramaView.Options();
        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        packageManager =  getContext().getPackageManager();
        hayGiroscopio = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        if(!hayGiroscopio)
            showWarning(getActivity(),R.string.noVirtual);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;
        Uri urlImg;

        inflatedView = inflater.inflate(R.layout.raco_seleccionado_layout, container, false);
        ButterKnife.bind(this,inflatedView);
        urlImg = Uri.parse(pano.getUriStr());
        Glide
                .with(this)
                .load(urlImg)
                .asBitmap()
                .placeholder(R.drawable.escudo)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        panoWidgetView.loadImageFromBitmap(resource, panoOptions);
                    }
                });
        return inflatedView;
    }
}
