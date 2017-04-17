package cf.castellon.turistorre.fragments.Click;

import android.content.pm.PackageManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import static cf.castellon.turistorre.utils.Utils.*;

public class TerratSeleccionado extends Fragment  {
    Imagen pano;
    Usuario usuario;
    private VrPanoramaView.Options panoOptions;
    @BindView(R.id.vrTerrat) VrPanoramaView panoWidgetView;
    @BindView(R.id.tvTitTerrat) TextView tvTitulo;
    @BindView(R.id.ivAvatarTerrat) ImageView ivAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean hayGiroscopio;
        PackageManager packageManager;

        pano = getArguments().getParcelable("imagen");
        usuario = getArguments().getParcelable("usuario");
        super.onCreate(savedInstanceState);
        panoOptions = new VrPanoramaView.Options();
        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        packageManager =  getContext().getPackageManager();
        hayGiroscopio = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        if(!hayGiroscopio)
            showWarning(getActivity(),"Tu móvil no está preparado para la Realidad Virtual!!");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;
        Uri urlImg;
        Uri urlAvatar;

        inflatedView = inflater.inflate(R.layout.terrat_seleccionado_layout, container, false);
        ButterKnife.bind(this,inflatedView);
        tvTitulo.setText(pano.getTitulo());
        urlImg = Uri.parse(pano.getUriStr());
        urlAvatar = Uri.parse(usuario.getAvatar());
        Glide.
                with(getContext()).
                load(urlAvatar).
                placeholder(R.drawable.escudo).
                into(ivAvatar);
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
