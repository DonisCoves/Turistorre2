package cf.castellon.turistorre.fragments.ActionBar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Bando;
import cf.castellon.turistorre.fragments.Principal.BandoRecyclerView;

import static cf.castellon.turistorre.utils.Utils.*;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.showProgressDialog;

public class GenerarBando extends Fragment {
    @BindView(R.id.etTituloBando) EditText etTitulo;
    @BindView(R.id.etDescBando) EditText etDescripcion;
    @BindView(R.id.ivGenerarBando) ImageView ivBando;
    private Activity mActivity;
    private StorageReference mStorageRef;
    private Bando bando;
    private SharedPreferences.Editor editor;

    @Override
    public void onAttach(Activity activity) {
        this.mActivity = activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generar_bando_layout,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activarServer(getContext());
    }

    @OnClick({R.id.btnConf,R.id.btnMemoria,R.id.btnCamara})
    public void onClick(View v){
        switch (v.getId()) {
            case (R.id.btnConf):
                Confirmacion();
                break;
            case (R.id.btnMemoria):
                buscarFotoMem();
                break;
            case (R.id.btnCamara):
                /*if (numPermisos==2)
//                    goCamera(this);
                else
                    pedirPermiso(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, etTitulo);*/
                break;
        }
    }

    private void Confirmacion() {
        /*if (!etTitulo.getText().toString().isEmpty() && fileUri!=null){
            mStorageRef = mStorageBandoRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(fileUri.getLastPathSegment());
            showProgressDialog(mActivity);
            mStorageRef.putBytes(escalarImagen(fileUri,1.4f))
                    .addOnSuccessListener(mActivity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            bando = new Bando(mFirebaseUser.getUid(),etTitulo.getText().toString(),etDescripcion.getText().toString(),taskSnapshot.getDownloadUrl().toString(),obtenerFecha());
                            guardarBandoBBDDFire();
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
        } else {
            Toast.makeText(getContext(),"Rellena el título y pon una imagen",Toast.LENGTH_SHORT).show();
        }*/
  }

    private void guardarBandoBBDDFire() {
        try{
            final DatabaseReference dbRef = mDataBaseBandoRef.push();
            dbRef.setValue(bando)
                    .addOnSuccessListener(mActivity, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame,new BandoRecyclerView()).commit();
                            generarNotificacionBando(bando.getTitulo(),bando.getFecha(),dbRef.getKey());
//                            fileUri=null;
                            Log.d("guardarBandoBBDDFire","Bando guardado en BBDD");
                        }
                    })
                    .addOnFailureListener(mActivity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e(TAG, "uploadFromUri:onFailure", exception);
                            hideProgressDialog();
                            Toast.makeText(mActivity, "Error: no se ha subido el bando",Toast.LENGTH_SHORT).show();
//                            fileUri=null;
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame,new BandoRecyclerView()).commit();
                        }
                    });
        }catch (Exception e){
            e.getMessage();
        }

    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap=null;
        if (requestCode==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==mActivity.RESULT_OK){
            bitmap = BitmapFactory.decodeFile(fileUri.getPath());
            ivBando.setImageBitmap(bitmap);
        } else if (requestCode==CAPTURE_IMAGE_GALLERY_ACTIVITY_REQUEST_CODE && resultCode==mActivity.RESULT_OK && data!=null){
            Uri imagenSeleccionada = data.getData();
            String [] rutaColumna = {MediaStore.Images.Media.DATA};
            Cursor cursor = mActivity.getContentResolver().query(imagenSeleccionada,rutaColumna,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(rutaColumna[0]);
            String imagenADecodificar = cursor.getString(columnIndex);
            fileUri = Uri.parse(imagenADecodificar);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(imagenADecodificar);
            ivBando.setImageBitmap(bitmap);
        }

    }*/

    private void buscarFotoMem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CAPTURE_IMAGE_GALLERY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISO_CAMARA :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permiso  camara concedido. Permisos vigentes " + ++numPermisos);
                }
                else {
                    Log.i(TAG, "Permiso denegado. Permisos vigentes " + numPermisos);
                }
                break;
            case PERMISO_ESCRIBIR_SD :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permiso  escritura concedido. Permisos vigentes " + ++numPermisos);
//                    pedirPermiso(this, Manifest.permission.CAMERA, PERMISO_CAMARA, etTitulo);
                }
                else {
                    Log.i(TAG, "Permiso denegado. Permisos vigentes " + numPermisos);
                }
                break;
        }
		   editor = prefs.edit();
        editor.putInt("numPermisos", numPermisos);
        editor.commit();
        if (numPermisos==2) {
//            goCamera(this);
        }
    }

}
