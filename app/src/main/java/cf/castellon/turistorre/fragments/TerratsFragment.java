package cf.castellon.turistorre.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterTerratsRecyclerView;
import cf.castellon.turistorre.bean.Panoramica;
import cf.castellon.turistorre.bean.Usuario;

import static android.content.Context.SENSOR_SERVICE;
import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

/**
 * Created by pccc on 18/12/2016.
 */

public class TerratsFragment extends Fragment {
    private RecyclerView recView;
    private Activity mActivity;
    private StorageReference mStorageRefPre;
    private StorageReference mStorageRef;
    private Panoramica pano;
    private MyFireAdapterTerratsRecyclerView adaptador;
    private LinearLayout layout;
    private LinearLayoutManager  manager;
    private FragmentManager fragmentManager;
    private TerratSeleccionadoFragment terratSeleccionadoFragment;
    private Bundle bund;
    private FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptador = new MyFireAdapterTerratsRecyclerView(Panoramica.class, R.layout.fila_fire_terrat_recycle,MyFireAdapterTerratsRecyclerView.MyFireViewHolder.class,mDataBaseTerratsRef,mActivity);
        manager = new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        fragmentManager=getChildFragmentManager();
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
        View view = inflater.inflate(R.layout.terrats_layout,container,false);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView = (RecyclerView) view.findViewById(R.id.rvTerrats);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Panoramica pano = adaptador.getItem(recView.getChildAdapterPosition(v));
                bund = new Bundle();
                bund.putString("TITULO", pano.getTitulo());
                Usuario usuario = buscarUsuario(pano.getUidUser());
                bund.putString("AVATAR", usuario.getAvatar());
                bund.putString("PANO_URL_STR", pano.getUriStr());
                terratSeleccionadoFragment = new TerratSeleccionadoFragment();
                terratSeleccionadoFragment.setArguments(bund);
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, terratSeleccionadoFragment).commit();
            }
        });
        recView.setAdapter(adaptador);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_terrats, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser !=null)
            switch (item.getItemId()){
                case R.id.it_gal_terrats:
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,new GenerarTerrat()).commit();
            }
        else
            Toast.makeText(getContext(),"Usuario no registrado",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}