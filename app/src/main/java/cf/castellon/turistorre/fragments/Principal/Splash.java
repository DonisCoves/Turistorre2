package cf.castellon.turistorre.fragments.Principal;

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
import java.util.HashSet;
import java.util.List;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.DiaFiesta;
import cf.castellon.turistorre.bean.Fiestas;
import cf.castellon.turistorre.bean.Imagen;
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
        cargas.add(CARGAR_DIAFIESTA);
        cargas.add(CARGAR_USUARIO);
        cargas.add(CARGAR_USUARIOS);
        cargas.add(CARGAR_IMAGENES);
        cargas.add(CARGAR_TERRATS);
        cargas.add(CARGAR_RACONS);
        cargas.add(CARGAR_HOME);
        cargarDatos(cargas);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    private void cargarDatos(List<Integer> cargas) {
        final HashSet<Imagen> imagenes;
        final HashSet<Imagen> terrats;
        final HashSet<Imagen> racons;
        final HashSet<Usuario> usuarios;
        final HashSet<Fiestas> fiestasHash;
        final HashSet<DiaFiesta> diaFiestasHash;

        imagenes = new HashSet<>();
        terrats = new HashSet<>();
        racons = new HashSet<>();
        usuarios = new HashSet<>();
        fiestasHash = new HashSet<>();
        diaFiestasHash = new HashSet<>();
        for (int carga : cargas) {
            switch (carga) {
                case CARGAR_RACONS:
                    mDataBaseRacoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Imagen raco = postSnapshot.getValue(Imagen.class);
                                racons.add(raco);
                            }
                            baseDatos.put(Tablas.Racons.name(),racons);
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
                            baseDatos.put(Tablas.Imagenes.name(),imagenes);
                            goToHome();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    break;
                case CARGAR_FIESTAS:
                    mDataBaseFiestasRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot fiestasFire : dataSnapshot.getChildren()) {
                                Fiestas fiestas = fiestasFire.getValue(Fiestas.class);
                                fiestasHash.add(fiestas);
                            }
                            baseDatos.put(Tablas.Fiestas.name(),fiestasHash);
                            goToHome();
                        }

                        @Override
                        public void onCancelled(DatabaseError arg0) {
                        }
                    });
                    break;
                case CARGAR_DIAFIESTA:
                    mDataBaseDiasFiestaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot diaFiestaFire : dataSnapshot.getChildren()) {
                                DiaFiesta diaFiesta = diaFiestaFire.getValue(DiaFiesta.class);
                                diaFiestasHash.add(diaFiesta);
                            }
                            baseDatos.put(Tablas.DiasFiestas.name(),diaFiestasHash);
                            goToHome();
                        }
                        @Override
                        public void onCancelled(DatabaseError arg0) {
                        }
                    });
                    break;
                case CARGAR_USUARIO:  //Igual no fa falta
                    String uid = prefs.getString("uidUser", "");
                    if (!uid.isEmpty())
                        mDataBaseUsersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                usuario = dataSnapshot.getValue(Usuario.class);
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
                                usuarios.add(usuario);
                            }
                            baseDatos.put(Tablas.Usuarios.name(),usuarios);
                            goToHome();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    break;
                case CARGAR_TERRATS:
                    mDataBaseTerratRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Imagen panoramica = postSnapshot.getValue(Imagen.class);
                                terrats.add(panoramica);
                            }
                            baseDatos.put(Tablas.Terrats.name(),terrats);
                            goToHome();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    break;
                case CARGAR_HOME:
                    mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                    mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
                    mFirebaseRemoteConfig.fetch().addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFirebaseRemoteConfig.activateFetched();
                            portadaRC = mFirebaseRemoteConfig.getString("portada");
                            usuarioUidRC = mFirebaseRemoteConfig.getString("uidUser");
                            goToHome();
                        }
                    });
            }
        }
    }

    public void goToHome() {
        FragmentTransaction transaccion;
        Fragment fragment;
        int numMaxCargas;

        numMaxCargas = cargas.size();
        transaccion = getFragmentManager().beginTransaction();
        fragment = new HomeViewPager();
        numCarga++;
        hideProgressDialog();
        if (numCarga == numMaxCargas) {
            transaccion.replace(R.id.content_frame, fragment).commit();
        }
    }
}
