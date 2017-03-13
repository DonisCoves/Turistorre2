package cf.castellon.turistorre.pruebas;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.panorama.Panorama;
import com.google.android.gms.panorama.PanoramaApi;

import cf.castellon.turistorre.R;

import static cf.castellon.turistorre.utils.Constantes.CAPTURE_TERRAT_GALLERY_ACTIVITY_REQUEST_CODE;
import static cf.castellon.turistorre.utils.Utils.fileUri;

public class PanoramaActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient gacClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gacClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(Panorama.API)
                .build();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CAPTURE_TERRAT_GALLERY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int columnIndex;
        Cursor cursor;
        String imagenADecodificar;

        if (requestCode==CAPTURE_TERRAT_GALLERY_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            fileUri = data.getData();
            String [] rutaColumna= {MediaStore.Images.Media.DATA};
            cursor= getContentResolver().query(fileUri,rutaColumna,null,null,null);
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(rutaColumna[0]);
            imagenADecodificar = cursor.getString(columnIndex);
            fileUri = Uri.parse(imagenADecodificar);
            cursor.close();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        gacClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Panorama.PanoramaApi.loadPanoramaInfo(gacClient, fileUri).setResultCallback(
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
    public void onStop() {
        super.onStop();
        gacClient.disconnect();
    }
}
