package cf.castellon.turistorre.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Fiestas;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Panoramica;
import cf.castellon.turistorre.bean.Raco;
import cf.castellon.turistorre.bean.Usuario;


//tiempoDatosRecogidos = System.currentTimeMillis();
//        tiempoEspera = (tiempoDatosRecogidos - tiempoInicialWeb)/100;//3532
public class Splash extends Fragment {
    List<Integer> cargas;
    static int numCarga;  //El numero de la carga actual

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargas = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_layout, container, false);
        showProgressDialog(getContext(), "Cargando");
        cargas.add(CARGAR_FIESTAS);
        cargas.add(CARGAR_USUARIO);
        cargas.add(CARGAR_USUARIOS);
        cargas.add(CARGAR_IMAGENES);
        cargas.add(CARGAR_RACONS);
        cargas.add(CARGAR_HOME);
        cargas.add(CARGAR_TERRATS);
        cargarDatos(cargas);
        return view;
    }

    private void cargarDatos(List<Integer> cargas) {
        for (int carga : cargas) {
            switch (carga) {
                case CARGAR_RACONS:
                    mDataBaseRaconsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Raco raco = postSnapshot.getValue(Raco.class);
                                racons.add(raco);
                            }
                            goToHome();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    break;
                case CARGAR_IMAGENES:
                    mDataBaseImgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Imagen imagen = postSnapshot.getValue(Imagen.class);
                                imagenes.add(imagen);
                            }
                            goToHome();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    break;
                case CARGAR_FIESTAS:
                    mDataBaseFiestasRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot fiestasList : dataSnapshot.getChildren()) {
                                Fiestas fiestas = fiestasList.getValue(Fiestas.class);
                                fiestasMap.put(fiestasList.getKey(), fiestas);
                            }
                            goToHome();
                        }

                        @Override
                        public void onCancelled(DatabaseError arg0) {
                        }
                    });
                    break;
                case CARGAR_USUARIO:
                    String uid = prefs.getString("uidUser", "");
                    if (!uid.isEmpty())
                        mDataBaseUsersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                usuario = dataSnapshot.getValue(Usuario.class);
                                hideProgressDialog();
                                goToHome();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    break;
                case CARGAR_USUARIOS:
                    mDataBaseUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Usuario usuario = postSnapshot.getValue(Usuario.class);
                                anyadirUsuarioLocal(usuario);
                            }
                            goToHome();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    break;
                case CARGAR_TERRATS:
                    mDataBaseTerratsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Panoramica panoramica = postSnapshot.getValue(Panoramica.class);
                                panoramicas.add(panoramica);
                            }
                            goToHome();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    break;
                case CARGAR_HOME:
                    //Cargamos portadas mostrando racons
                    List<String> raconsList;
                    raconsList = new ArrayList<>();
                    for (Raco raco:racons) {
                        raconsList.add(raco.getUrl());
                    }
                    homes.put("racons",raconsList);
                    //Cargamos portadas mostrando terrats
                    List<String> terratsList;
                    terratsList = new ArrayList<>();
                    for (Panoramica terrat:panoramicas) {
                        terratsList.add(terrat.getUriStr());
                    }
                    homes.put("terrats",terratsList);
                    //Cargamos portadas mostrando imagenes de determinados(a definir despues) usuarios
                    Map<String,List<String>> usuarioImagenesMap;
                    usuarioImagenesMap = new HashMap<>();
                    for (Usuario user:usuarios) {
                        usuarioImagenesMap.put(user.getUidUser(),buscarImagenes(user.getUidUser()));
                    }
                    homes.put("usuarios",usuarioImagenesMap);
                    //Cargamos portadas mostrando imagenes de determinados(a definir despues) usuarios

                    establecerPortada(getActivity());

            }

        }
    }

    public void establecerPortada(Activity activity) {
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        portadaRC = mFirebaseRemoteConfig.getString("portada");
        usuarioUidRC = mFirebaseRemoteConfig.getString("uidUser");
        mFirebaseRemoteConfig.fetch().addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFirebaseRemoteConfig.activateFetched();
                goToHome();
            }
        });


    }

    public void goToHome() {
        FragmentTransaction transaccion;
        Fragment fragment;
        int numMaxCargas;

        numMaxCargas = cargas.size();
        transaccion = getFragmentManager().beginTransaction();
        fragment = new HomeFragment();
        if (numCarga == numMaxCargas)
            transaccion.replace(R.id.content_frame, fragment).commit();
    }
}
