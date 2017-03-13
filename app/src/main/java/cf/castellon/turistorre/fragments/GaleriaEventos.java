package cf.castellon.turistorre.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterGaleriaEventoRecyclerView;
import cf.castellon.turistorre.bean.Imagen;
import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

/**
 * Created by pccc on 02/03/2017.
 */

public class GaleriaEventos extends Fragment {
    private MyFireAdapterGaleriaEventoRecyclerView adaptador;
    private GridLayoutManager manager;
    private FirebaseUser mFirebaseUser ;
    private boolean datosImagenOk;
    @Bind(R.id.rvGaleriaEventos) RecyclerView recView;
    private Imagen mImagen;
    private String uidEvento,uidDiaFiesta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> uidsImagenes;
        DatabaseReference ref;

        uidEvento = getArguments().getString("UIDEvento");
        uidDiaFiesta = getArguments().getString("UIDDia");
//        uidsImagenes = getArguments().getStringArrayList("UIDImagenes");
        ref = mDataBaseDiasFiestaRef.child(uidDiaFiesta).child("eventos").child(uidEvento).child("imagenes");
        adaptador = new MyFireAdapterGaleriaEventoRecyclerView(Imagen.class,
                                                               R.layout.fila_eventos_galeria,
                                                               MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder.class,
                                                               ref
                                                               );
        manager = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eventos_galeria_layout,container,false);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(manager);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagen imagen = adaptador.getItem(recView.getChildPosition(v));
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                CarruselGaleriaFragment carruselGaleriaFragment = new CarruselGaleriaFragment();
                Bundle bund = new Bundle();
                bund.putString("UID_IMG", imagen.getUidImg());
                carruselGaleriaFragment.setArguments(bund);
                fragmentTransaction.replace(R.id.content_frame, carruselGaleriaFragment).commit();
            }
        });
        recView.setAdapter(adaptador);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_eventos, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser!=null)
            switch (item.getItemId()){
                case R.id.it_camera:
                    if (numPermisos==2)
                        goCamera(this);
                    else
                        pedirPermiso3(this, new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},new int []{PERMISO_ESCRIBIR_SD,PERMISO_CAMARA} , recView);
                    break;
                case R.id.it_galeriaEventos:
                    buscarFotoMem();
                    break;
            }
        else
            Toast.makeText(getContext(),"Registrate para enviar fotos",Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== Activity.RESULT_OK){ //Venimos de la camara
            guardarFotoStorageFire();
        } else if (requestCode==CAPTURE_IMAGE_GALLERY_ACTIVITY_REQUEST_CODE
                    &&   // Venimos de la galeria
                   resultCode==Activity.RESULT_OK && data!=null)  {
            Uri imagenSeleccionada = data.getData();
            String [] rutaColumna = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(imagenSeleccionada,rutaColumna,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(rutaColumna[0]);
            String imagenADecodificar = cursor.getString(columnIndex);
            fileUri = Uri.parse(imagenADecodificar);
            cursor.close();
            guardarFotoStorageFire();
        }
    }

    private void guardarFotoStorageFire() {
        datosImagenOk = false;
        StorageReference mStorageRef, mStorageRefPre ;
        String pathPre;

        mStorageRef= mStorageFiestasRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(fileUri.getLastPathSegment());
        pathPre = fileUri.getLastPathSegment();
        pathPre = pathPre.substring(0,pathPre.length()-5);
        pathPre = pathPre.concat("_PRE.jpg");
        mStorageRefPre = mStorageImgRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(pathPre);
        showProgressDialog(getActivity());
        Log.d(TAG, "uploadFromUri:dst:" + mStorageRef.getPath());
        mStorageRefPre.putBytes(escalarImagen(fileUri,1/8f)) //Pre
                .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (datosImagenOk) {
                            mImagen.setUriStrPre(taskSnapshot.getDownloadUrl().toString());
                            guardarFotoBBDDFire(mImagen);
                            datosImagenOk=false;
                        }
                        else{
                            mImagen = new Imagen();
                            mImagen.setUidUser(mFirebaseUser.getUid());
                            mImagen.setUriStrPre(taskSnapshot.getDownloadUrl().toString());
                            mImagen.setUidImg(fileUri.getLastPathSegment().substring(0,fileUri.getLastPathSegment().length()-4));
                            datosImagenOk =true;
                        }
                        Log.d(TAG, "uploadFromUri:onSuccess");
                        hideProgressDialog();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        Toast.makeText(getActivity(), "Error: no se ha subido la imagen",Toast.LENGTH_SHORT).show();
                    }
                });

        mStorageRef.putBytes(escalarImagen(fileUri,1/4f))
                .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "uploadFromUri:onSuccess");
                        hideProgressDialog();
                        if (datosImagenOk) {
                            mImagen.setUriStr(taskSnapshot.getDownloadUrl().toString());
                            guardarFotoBBDDFire(mImagen);
                            datosImagenOk=false;
                        }
                        else {
                            mImagen = new Imagen();
                            mImagen.setUidUser(mFirebaseUser.getUid());
                            mImagen.setUidImg(fileUri.getLastPathSegment().substring(0,fileUri.getLastPathSegment().length()-4));
                            mImagen.setUriStr(taskSnapshot.getDownloadUrl().toString());
                            datosImagenOk = true;
                        }
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        Toast.makeText(getActivity(), "Error: no se ha subido la imagen",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarFotoBBDDFire(Imagen imagen) {
        DatabaseReference dbRef;
        dbRef = mDataBaseDiasFiestaRef.child(uidDiaFiesta).child("eventos").child(uidEvento).child("imagenes").child(imagen.getUidImg());
        try {
            dbRef.setValue(mImagen).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    hideProgressDialog();
                    Log.e("guardarFotoBBDDFire","Foto guardada en BBDD");
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("guardarFotoBBDDFire","Error: "+e.getMessage());
                    hideProgressDialog();
                }
            });
        }
        catch (DatabaseException e) {
            Log.e(TAG,e.getMessage());
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
                }
                else {
                    Log.i(TAG, "Permiso denegado. Permisos vigentes " + numPermisos);
                }
                break;
        }
        if (numPermisos==2) {
            goCamera(this);
        }
    }

}
