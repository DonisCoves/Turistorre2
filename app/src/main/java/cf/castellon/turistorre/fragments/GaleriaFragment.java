package cf.castellon.turistorre.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import static cf.castellon.turistorre.utils.Utils.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.adaptadores.MyFireAdapterGaleriaRecyclerView;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

import static cf.castellon.turistorre.utils.Constantes.*;

public class GaleriaFragment extends Fragment {
    private RecyclerView recView;
    private Activity mActivity;
    private StorageReference mStorageRefPre;
    private StorageReference mStorageRef;
    private Imagen mImagen;
    private MyFireAdapterGaleriaRecyclerView adaptador;
    private LinearLayout layout;
    private GridLayoutManager manager;
    private CarruselGaleriaFragment carruselGaleriaFragment;
    private Bundle bund;
    private FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptador = new MyFireAdapterGaleriaRecyclerView(Imagen.class,R.layout.fila_fire_recycle,MyFireAdapterGaleriaRecyclerView.MyFireViewHolder.class,mDataBaseImgRef,mActivity);
        manager = new GridLayoutManager(mActivity,3,GridLayoutManager.VERTICAL,false);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fileUri = null; //Hay otros fragments que utilizan esta variable
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.galeria_layout,container,false);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView = (RecyclerView) view.findViewById(R.id.rvGaleria);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagen imagen = adaptador.getItem(recView.getChildPosition(v));
                fragmentTransaction = getFragmentManager().beginTransaction();
                carruselGaleriaFragment = new CarruselGaleriaFragment();
                bund = new Bundle();
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
        inflater.inflate(R.menu.menu_galeria, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser !=null)
            switch (item.getItemId()){
                case R.id.it_foto:
                    pedirPermiso(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, recView);
                    pedirPermiso(mActivity, Manifest.permission.CAMERA, PERMISO_CAMARA, recView);
                    if (numPermisos==2)
                        goCamera(this);
            }
        return super.onOptionsItemSelected(item);
    }

    private void guardarFotoStorageFire() {
        datosImagenOk=false;
        mStorageRef = mStorageImgRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(fileUri.getLastPathSegment());
        String pathPre = fileUri.getLastPathSegment();
        pathPre = pathPre.substring(0,pathPre.length()-5);
        pathPre = pathPre.concat("_PRE.jpg");
        mStorageRefPre = mStorageImgRef.child(mFirebaseUser.getDisplayName()+" - "+mFirebaseUser.getUid()).child(pathPre);
        showProgressDialog(mActivity);
        Log.d(TAG, "uploadFromUri:dst:" + mStorageRef.getPath());
        mStorageRefPre.putBytes(escalarImagen(fileUri,1/8f)) //Pre
                .addOnSuccessListener(mActivity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        Toast.makeText(mActivity, "Error: no se ha subido la imagen",Toast.LENGTH_SHORT).show();
                    }
                });

        mStorageRef.putBytes(escalarImagen(fileUri,1/4f))
                .addOnSuccessListener(mActivity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        Toast.makeText(mActivity, "Error: no se ha subido la imagen",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==mActivity.RESULT_OK){
            guardarFotoStorageFire();
        }
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

    private void guardarFotoBBDDFire(Imagen imagen) {
        DatabaseReference dbRef = mDataBaseImgRef.child(imagen.getUidImg());
        try {
            dbRef.setValue(mImagen).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    hideProgressDialog();
                    Log.d("guardarFotoBBDDFire","Foto guardada en BBDD");
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("guardarFotoBBDDFire","Error: "+e.getMessage());
                    hideProgressDialog();
                }
            });
        }
        catch (DatabaseException e) {
            Log.e(TAG,e.getMessage());
        }
    }


}