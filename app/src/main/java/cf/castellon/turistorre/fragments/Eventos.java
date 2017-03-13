package cf.castellon.turistorre.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterEventosRecyclerView;
import cf.castellon.turistorre.bean.Evento;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.ImagenParcel;

import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;

/**
 * Created by pccc on 28/02/2017.
 */

public class Eventos extends Fragment {
    private String uidDiaFiestaMeta;
    private MyFireAdapterEventosRecyclerView adaptador;
    private LinearLayoutManager manager;
    @Bind(R.id.rvEventos) RecyclerView recView;
    private Query keysFire;
    private DatabaseReference dataRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        uidDiaFiestaMeta = getArguments().getString("UID");
        keysFire = mDataBaseKeysRef.child(uidDiaFiestaMeta).orderByValue();
        dataRef = mDataBaseDiasFiestaRef.child(uidDiaFiestaMeta).child("eventos");
        adaptador = new MyFireAdapterEventosRecyclerView(Evento.class,
                                                         R.layout.fila_eventos_layout,
                                                         MyFireAdapterEventosRecyclerView.MyFireViewHolder.class,
                                                         keysFire,
                                                         dataRef);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eventos_layout, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Evento evento = adaptador.getItem(recView.getChildAdapterPosition(v));
//                Map<String, Imagen> imagenesMap = evento.getImagenes();
//                Iterator<String> itImagenes = imagenesMap.keySet().iterator();
//                ArrayList<String> imagenesIds = new ArrayList<>();
//                while (itImagenes.hasNext()) {
//                    imagenesIds.add(itImagenes.next());
//                }
                Bundle bund = new Bundle();
                bund.putString("UIDDia", uidDiaFiestaMeta);
                bund.putString("UIDEvento", evento.getUidEvento());
//                bund.putStringArrayList("UIDImagenes", imagenesIds);
                GaleriaEventos galeriaEventos = new GaleriaEventos();
                galeriaEventos.setArguments(bund);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, galeriaEventos).commit();
            }
        });
        recView.setAdapter(adaptador);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dia, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.it_galeriaDias:
                    DatabaseReference ref = mDataBaseDiasFiestaRef.child(uidDiaFiestaMeta).child("eventos");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<ImagenParcel> imagenes=new ArrayList<>();
                            for (DataSnapshot eventoDS: dataSnapshot.getChildren()) {
                                for(DataSnapshot imagenDS: eventoDS.child("imagenes").getChildren()){
                                    ImagenParcel imagen = imagenDS.getValue(ImagenParcel.class);
                                    imagenes.add(imagen);
                                }
                            }
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            GaleriaDiaFragment galeriaDiaFragment = new GaleriaDiaFragment();
                            Bundle bund = new Bundle();
                            bund.putParcelableArrayList("imagenes",imagenes);
//                            bund.putputStringArrayList("URLs", imagenesURL);
//                            bund.putStringArrayList("PRE_URLs", imagenesPreURl);
                            galeriaDiaFragment.setArguments(bund);
                            fragmentTransaction.replace(R.id.content_frame, galeriaDiaFragment).commit();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
            }
        return super.onOptionsItemSelected(item);
    }

}