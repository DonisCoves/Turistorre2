package cf.castellon.turistorre.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.panorama.Panorama;
import com.google.android.gms.panorama.PanoramaApi;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import cf.castellon.turistorre.R;

/**
 * Created by pccc on 25/03/2016.
 */

public class TerratSeleccionadoFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    String url_img;
    String titulo;
    String url_avatar;
    Uri urlImg;
    Bundle args;
    private VrPanoramaView panoWidgetView;
    private VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();
    private GoogleApiClient gacClient;
    private PackageManager packageManager;
    private boolean hayGiroscopio;
    public static final String PANORAMA_URI = "panorama_uri";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        args = getArguments();
        url_img = args.getString("PANO_URL_STR");
        titulo = args.getString("DIRECCION");
        url_avatar = args.getString("AVATAR");
        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        packageManager =  getContext().getPackageManager();
        hayGiroscopio = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        if(!hayGiroscopio) {
//            Toast.makeText(getContext(),"Tu móvil no está preparado para la Realidad Virtual!!",Toast.LENGTH_LONG).show();
//            gacClient= new GoogleApiClient.Builder(getContext(), this, this)
//                    .addApi(Panorama.API)
//                    .build();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;
        TextView tvTitulo;
        ImageView ivAvatar;

        inflatedView = inflater.inflate(R.layout.terrat_seleccionado_layout, container, false);

        panoWidgetView = (VrPanoramaView) inflatedView.findViewById(R.id.vrTerrat);
        tvTitulo = (TextView) inflatedView.findViewById(R.id.tvTitTerrat);
        ivAvatar = (ImageView) inflatedView.findViewById(R.id.ivAvatarTerrat);

        tvTitulo.setText(titulo);
        urlImg = Uri.parse(url_img);
        Uri urlAvatar = Uri.parse(url_avatar);
        Glide.
                with(getContext()).
                load(urlAvatar).
                placeholder(R.drawable.escudo).
                into(ivAvatar);

        Glide
                .with(this) // safer!
                .load(url_img)
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

    @Override
    public void onConnected(Bundle connectionHint) {
//        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/DCIM/Camera/panoramica.jpg");
//        Uri uri = Uri.parse("file:///assets/panoramica.jpg");
        //      Uri uri = Uri.parse("android.resource://cf.castellon.turistorre/drawable/andes");
//        Uri uri = Uri.parse("file:///assets/panoramica.jpg" );
//
//        Panorama.PanoramaApi.loadPanoramaInfo(gacClient, uri).setResultCallback(
//                new ResultCallback<PanoramaApi.PanoramaResult>() {
//                    @Override
//                    public void onResult(PanoramaApi.PanoramaResult result) {
//                        Intent i;
//                        if (result.getStatus().isSuccess() && (i = result.getViewerIntent()) != null) {
//                            startActivity(i);
//                        } else {
//                            // Handle unsuccessful result
//                        }
//                    }
//                });

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // Handle connection being suspended
    }

    @Override
    public void onConnectionFailed(ConnectionResult status) {
        // Handle connection failure.
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(!hayGiroscopio) {
//            gacClient.connect();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if(!hayGiroscopio) {
//            gacClient.disconnect();
//        }
    }
}
