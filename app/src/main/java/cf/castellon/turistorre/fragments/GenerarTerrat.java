package cf.castellon.turistorre.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Bando;
import cf.castellon.turistorre.bean.Panoramica;

import static cf.castellon.turistorre.utils.Constantes.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static cf.castellon.turistorre.utils.Constantes.CAPTURE_IMAGE_GALLERY_ACTIVITY_REQUEST_CODE;
import static cf.castellon.turistorre.utils.Constantes.CAPTURE_TERRAT_GALLERY_ACTIVITY_REQUEST_CODE;
import static cf.castellon.turistorre.utils.Constantes.PERMISO_CAMARA;
import static cf.castellon.turistorre.utils.Constantes.PERMISO_ESCRIBIR_SD;
import static cf.castellon.turistorre.utils.Constantes.TAG;
import static cf.castellon.turistorre.utils.Constantes.mDataBaseBandoRef;
import static cf.castellon.turistorre.utils.Constantes.mDataBaseKeysRef;
import static cf.castellon.turistorre.utils.Constantes.mDataBaseRootRef;
import static cf.castellon.turistorre.utils.Constantes.mDataBaseTerratsRef;
import static cf.castellon.turistorre.utils.Constantes.mStorageBandoRef;
import static cf.castellon.turistorre.utils.Constantes.mStorageTerratsRef;
import static cf.castellon.turistorre.utils.Utils.activarServer;
import static cf.castellon.turistorre.utils.Utils.datosImagenOk;
import static cf.castellon.turistorre.utils.Utils.escalarImagen;
import static cf.castellon.turistorre.utils.Utils.escalarPanoramica;
import static cf.castellon.turistorre.utils.Utils.fileUri;
import static cf.castellon.turistorre.utils.Utils.generarNotificacionBando;
import static cf.castellon.turistorre.utils.Utils.generarNotificacionTerrat;
import static cf.castellon.turistorre.utils.Utils.goCamera;
import static cf.castellon.turistorre.utils.Utils.hideProgressDialog;
import static cf.castellon.turistorre.utils.Utils.mFirebaseUser;
import static cf.castellon.turistorre.utils.Utils.numPermisos;
import static cf.castellon.turistorre.utils.Utils.obtenerFecha;
import static cf.castellon.turistorre.utils.Utils.pedirPermiso;
import static cf.castellon.turistorre.utils.Utils.prefs;
import static cf.castellon.turistorre.utils.Utils.showProgressDialog;

public class GenerarTerrat extends Fragment {
    @Bind(R.id.etDirTerrat) EditText etDireccion;
    @Bind(R.id.ivGenTerrat) ImageView ivTerrat;
    private StorageReference mStorageRefPre;
    private FragmentTransaction fragmentTransaction;
    private SharedPreferences.Editor editor;

    private Activity mActivity;
    private StorageReference mStorageRef;
    private Panoramica pano;

    @Override
    public void onAttach(Activity activity) {
        this.mActivity = activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generar_terrat_layout,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activarServer(getContext());
    }

    @OnClick({R.id.btnGalTerrat,R.id.btnEnvTerrat,R.id.btnCanTerrat})
    public void onClick(View v){
        switch (v.getId()) {
            case (R.id.btnEnvTerrat):
                guardarPanoStorageFire(fileUri);
                break;
            case (R.id.btnGalTerrat):
                if (numPermisos==2) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, CAPTURE_TERRAT_GALLERY_ACTIVITY_REQUEST_CODE);
                }
                else
                    pedirPermiso(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD,etDireccion);
                break;
            case (R.id.btnCanTerrat):
                etDireccion.setText("");
                ivTerrat.setImageBitmap(null);
                break;
        }
    }

    private void guardarPanoStorageFire(final Uri file) {
        datosImagenOk=false;
        mStorageRef = mStorageTerratsRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(file.getLastPathSegment());
        String pathPre = file.getLastPathSegment();
        pathPre = pathPre.substring(0,pathPre.length()-5);
        pathPre = pathPre.concat("_PRE.jpg");
        mStorageRefPre = mStorageTerratsRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(pathPre);
        showProgressDialog(mActivity,"Subiendo a ServerTorre");
        Log.d(TAG, "uploadFromUri:dst:" + mStorageRef.getPath());
        mStorageRefPre.putBytes(escalarImagen(file,1/8f)) //Pre
                .addOnSuccessListener(mActivity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (datosImagenOk) {
                            pano.setUriStrPre(taskSnapshot.getDownloadUrl().toString());
                            guardarPanoBBDDFire(pano);
                            datosImagenOk=false;
                        }
                        else{
                            pano = new Panoramica();
                            pano.setUidUser(mFirebaseUser.getUid());
                            pano.setUriStrPre(taskSnapshot.getDownloadUrl().toString());
                            pano.setUidImg(file.getLastPathSegment().substring(0,file.getLastPathSegment().length()-4));
                            pano.setTitulo(etDireccion.getText().toString());
                            datosImagenOk =true;
                        }
                        Log.d(TAG, "uploadFromUri:onSuccess");
                        //hideProgressDialog();
                    }
                })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        Toast.makeText(mActivity, "Error: no se ha subido la imagen",Toast.LENGTH_SHORT).show();
                    }
                });

        mStorageRef.putBytes(escalarPanoramica(file))
                .addOnSuccessListener(mActivity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "uploadFromUri:onSuccess");
                        //hideProgressDialog();
                        if (datosImagenOk) {
                            pano.setUriStr(taskSnapshot.getDownloadUrl().toString());
                            guardarPanoBBDDFire(pano);
                            datosImagenOk=false;
                        }
                        else {
                            pano = new Panoramica();
                            pano.setUidUser(mFirebaseUser.getUid());
                            pano.setUidImg(file.getLastPathSegment().substring(0,file.getLastPathSegment().length()-4));
                            pano.setUriStr(taskSnapshot.getDownloadUrl().toString());
                            pano.setTitulo(etDireccion.getText().toString());
                            datosImagenOk = true;
                        }
                    }
                })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        Toast.makeText(mActivity, "Error: no se ha subido la panoramica",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarPanoBBDDFire(final Panoramica pano) {
        DatabaseReference dbRef = mDataBaseTerratsRef.child(pano.getUidImg());
        try {
            dbRef.setValue(pano).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    hideProgressDialog();
                    Toast.makeText(getContext(),"Terrat subido con éxito!!",Toast.LENGTH_SHORT).show();
                    Log.d("guardarPanoBBDDFire","Panoramica guardada en BBDD");
                    generarNotificacionTerrat(pano.getTitulo(),pano.getUidImg());
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, new TerratsFragment()).commit();
                }
            }).addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("guardarPanoBBDDFire","Error: "+e.getMessage());
                    hideProgressDialog();
                }
            });
        }
        catch (DatabaseException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        int width, height, columnIndex;
        Cursor cursor;
        String imagenADecodificar;
        Matrix matrix;
        Bitmap rotatedBitmap;

        if (requestCode==CAPTURE_TERRAT_GALLERY_ACTIVITY_REQUEST_CODE && resultCode==mActivity.RESULT_OK && data!=null){
            fileUri = data.getData();
            String [] rutaColumna= {MediaStore.Images.Media.DATA};
            cursor= mActivity.getContentResolver().query(fileUri,rutaColumna,null,null,null);
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(rutaColumna[0]);
            imagenADecodificar = cursor.getString(columnIndex);
            fileUri = Uri.parse(imagenADecodificar);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(imagenADecodificar);
            width = bitmap.getWidth(); //2340
            height = bitmap.getHeight(); //4160
            matrix = new Matrix();
            if (height>width)
                matrix.postRotate(90);

            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,width, height, matrix, true);
            ivTerrat.setImageBitmap(rotatedBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISO_ESCRIBIR_SD :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permiso  escritura concedido. Permisos vigentes " + numPermisos);
                    numPermisos = 2;
                }
                else {
                    Log.i(TAG, "Permiso denegado. Permisos vigentes " + numPermisos);
                }
                break;
        }
		   editor = prefs.edit();
        editor.putInt("numPermisos", numPermisos);
        editor.commit();
    }


}
