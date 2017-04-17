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
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.fragments.Principal.RaconsViewPager;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

public class GenerarRaco extends Fragment {
    @BindView(R.id.etTituloRaco) EditText etTitulo;
    @BindView(R.id.etDescRaco) EditText etDescripcion;
    @BindView(R.id.ivGenerarRaco) ImageView ivRaco;
    private StorageReference mStorageRef;
    private Imagen raco;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generar_raco_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.btnCamaraRaco,R.id.btnMemoriaRaco,R.id.btnConfRaco,R.id.btnCancRaco})
    public void onClick(View v){
        switch (v.getId()) {
            case (R.id.btnConfRaco):
                Confirmacion();
                break;
            case (R.id.btnCancRaco):
                etTitulo.setText("");
                etDescripcion.setText("");
                ivRaco.setImageBitmap(null);
                break;
            case (R.id.btnMemoriaRaco):
                buscarFotoMem();  //Faltara preguntar permiso!
                break;
            case (R.id.btnCamaraRaco):
                /*if (numPermisos==2)
                    goCamera(this);
                else
                    pedirPermiso(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, etTitulo);*/
                break;
        }
    }

    private void Confirmacion() {
       /* if (!etTitulo.getText().toString().isEmpty() &&
                !etDescripcion.getText().toString().isEmpty() && fileUri!=null){
            mStorageRef = mStorageRaconsRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(fileUri.getLastPathSegment());
            showProgressDialog(getContext());
            mStorageRef.putBytes(escalarImagen(fileUri,1.4f))
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            raco = new Imagen();
                            raco.setUriStr(taskSnapshot.getDownloadUrl().toString());
                            raco.setUriStrPre(taskSnapshot.getDownloadUrl().toString());
                            raco.setTitulo(etTitulo.getText().toString());
                            raco.setDescripcion(etDescripcion.getText().toString());
                            raco.setUidImg(fileUri.getLastPathSegment().substring(0,fileUri.getLastPathSegment().length()-4));
                            raco.setUidUser(mFirebaseUser.getUid());
                            guardarRacoBBDDFire();
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e(TAG, "uploadFromUri:onFailure", exception);
                            hideProgressDialog();
                            Toast.makeText(getActivity(), "Error: no se ha subido el racon",Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(),"Rellena el título y pon una imagen",Toast.LENGTH_SHORT).show();
        }*/
    }

    private void guardarRacoBBDDFire() {
        try{
            final DatabaseReference dbRef = mDataBaseRacoRef.push();
            dbRef.setValue(raco)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            hideProgressDialog();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame,new RaconsViewPager()).commit();
//                            fileUri=null;
                            //racons.add(raco);
                            Log.d("guardarRacoBBDDFire","Raco guardado en BBDD");
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e(TAG, "uploadFromUri:onFailure", exception);
                            hideProgressDialog();
                            Toast.makeText(getActivity(), "Error: no se ha subido el raco",Toast.LENGTH_SHORT).show();
//                            fileUri=null;
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame,new RaconsViewPager()).commit();
                        }
                    });
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap=null;
        if (requestCode==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==Activity.RESULT_OK){
//            bitmap = BitmapFactory.decodeFile(fileUri.getPath()); //Entrem per la camara
            ivRaco.setImageBitmap(bitmap);
        } else if (requestCode==CAPTURE_IMAGE_GALLERY_ACTIVITY_REQUEST_CODE && resultCode==Activity.RESULT_OK && data!=null){
            Uri imagenSeleccionada = data.getData(); //Entrem per la galeria
            String [] rutaColumna = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(imagenSeleccionada,rutaColumna,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(rutaColumna[0]);
            String imagenADecodificar = cursor.getString(columnIndex);
//            fileUri = Uri.parse(imagenADecodificar);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(imagenADecodificar);
            ivRaco.setImageBitmap(bitmap);
        }
    }

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
