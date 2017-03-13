package cf.castellon.turistorre.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.panorama.Panorama;

import com.google.android.gms.panorama.*;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView.Options;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cf.castellon.turistorre.R;

import static com.facebook.GraphRequest.TAG;

public class VRActivity extends AppCompatActivity /*implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener */{
    private GoogleApiClient gacClient;

    private static final String TAG = AppCompatActivity.class.getSimpleName();
    /** Actual panorama widget. **/
    private VrPanoramaView panoWidgetView;
    /**
     * Arbitrary variable to track load status. In this example, this variable should only be accessed
     * on the UI thread. In a real app, this variable would be code that performs some UI actions when
     * the panorama is fully loaded.
     */
    public boolean loadImageSuccessful;
    /** Tracks the file to be loaded across the lifetime of this app. **/
    private Uri fileUri;
    /** Configuration information for the panorama. **/
    private Options panoOptions = new Options();
    private ImageLoaderTask backgroundImageLoaderTask;
    private String url = "https://firebasestorage.googleapis.com/v0/b/project-1031372115432573568.appspot.com/o/andes.jpg?alt=media&token=6dd63744-ac93-4ae9-9909-5768d3104418";
    private Bitmap mBitmap;
    private RequestQueue requestQueue;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);
        imageview = (ImageView)findViewById(R.id.imageView);
        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        panoWidgetView = (VrPanoramaView)findViewById(R.id.pano_view);
        requestQueue= Volley.newRequestQueue(this);
        Glide
                .with(this) // safer!
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        panoWidgetView.loadImageFromBitmap(resource, panoOptions);
                    }
                });

        /*ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                try {
                    imageview.setImageBitmap(response);
                    panoWidgetView.loadImageFromBitmap(response, panoOptions);

                }catch ( Exception e){
                    String a= "mierda";

                }

                mBitmap = response;
//                backgroundImageLoaderTask.execute();
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("mierda",error.getMessage());
            }
        });
// Añadir petición a la cola
//

        requestQueue.add(request);*//*
        backgroundImageLoaderTask = new ImageLoaderTask();*/


    }

    class ImageLoaderTask extends AsyncTask<Void, Void, Boolean> {
        InputStream istr = null;
        VrPanoramaView.Options panoOptions = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                istr = new URL(url).openStream();
                panoOptions = new VrPanoramaView.Options();
                panoOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            } catch (IOException e) {
                Log.e(TAG, "Could not decode default bitmap: " + e);
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
            if( istr!=null && panoOptions!=null){
                panoWidgetView.loadImageFromBitmap(BitmapFactory.decodeStream(istr), panoOptions);
             }

                istr.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close input stream: " + e);
            }
            super.onPostExecute(aBoolean);
        }
    }

/*    @Override
    public void onStart() {
        super.onStart();
        gacClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        gacClient.disconnect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = getIntent();
        Uri uri2 = Uri.parse("file:///assets/panoramica.jpg" );
        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/DCIM/Camera/PANO_20161215_090446.jpg");
        String fileUri = intent.getStringExtra("panorama_uri");
        Panorama.PanoramaApi.loadPanoramaInfo(gacClient, uri).setResultCallback(
                new ResultCallback<PanoramaApi.PanoramaResult>() {
                    @Override
                    public void onResult(PanoramaApi.PanoramaResult result) {
                        Intent i;
                        if (result.getStatus().isSuccess() && (i = result.getViewerIntent()) != null) {
                            startActivity(i);
                        } else {
                            // Handle unsuccessful result
                        }
                    }
                });

    }*/

   /* @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG,"connection being suspended"+ i);// Handle

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Handle connection failure.
        Log.e(TAG,connectionResult.getErrorMessage());

    }*/

}
