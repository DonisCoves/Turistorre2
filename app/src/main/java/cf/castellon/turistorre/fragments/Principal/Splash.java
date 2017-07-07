package cf.castellon.turistorre.fragments.Principal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.DiaFiesta;
import cf.castellon.turistorre.bean.Evento;
import cf.castellon.turistorre.bean.Fiestas;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;

public class Splash extends Fragment {
    List<Integer> cargas;
    static int numCarga;
    DiaFiesta diaFiesta;
    Map<String, Evento> eventos;
    Map<String, Imagen> mapImg;
    Iterator<String> itEvento;
    Iterator<String> itImagen;
    String uidEvento;
    Evento evento;
    Imagen img;
    HashSet<Usuario> usuarios;
    HashSet<Imagen> terrats;
    HashSet<Imagen> racons;
    HashSet<Imagen> imagenes;
    HashSet<Fiestas> fiestasHash;
    HashSet<DiaFiesta> diaFiestasHash;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargas = new ArrayList<>();
        fiestasHash = new HashSet<>();
        diaFiestasHash = new HashSet<>();
        imagenes = new HashSet<>();
        usuarios = new HashSet<>();
        terrats = new HashSet<>();
        racons = new HashSet<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_layout, container, false);
        showProgressDialog(getContext(), getString(R.string.cargando));
        cargas.add(CARGAR_HOME);
        cargarDatos(cargas);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    private void cargarDatos(List<Integer> cargas) {
        for (int carga : cargas) {
            switch (carga) {
                case CARGAR_HOME:
                    mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                    mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
                    mFirebaseRemoteConfig.fetch().addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFirebaseRemoteConfig.activateFetched();
                            portadaRC = mFirebaseRemoteConfig.getString("portada");
                            usuarioUidRC = mFirebaseRemoteConfig.getString("uidUser");
                            diaRC = mFirebaseRemoteConfig.getString("dia");
                            switch (portadaRC) {  //terrats,racons,imagenes,usuarios,diasfiestas
                                case ("Terrats"):
                                    mDataBaseTerratRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                Imagen terrat = postSnapshot.getValue(Imagen.class);
                                                terrats.add(terrat);
                                            }
                                            baseDatos.put(Tablas.Terrats.name(),terrats);
                                            goToHome();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            goToHome();
                                        }
                                    });
                                    break;
                                case ("Racons"):
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
                                            goToHome();
                                        }
                                    });
                                    break;
                                case ("Imagenes"):
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
                                            goToHome();
                                        }
                                    });
                                case ("DiasFiestas"):
                                    goToHome();
/*
                                    diaFiesta = getDiaFiesta(diaRC);
                                    if (diaFiesta!=null) {
                                        eventos = diaFiesta.getEventos();
                                        itEvento = eventos.keySet().iterator();
                                        while (itEvento.hasNext()) {
                                            uidEvento = itEvento.next();
                                            evento = eventos.get(uidEvento);
                                            mapImg = evento.getImagenes();
                                            if (mapImg!=null) {
                                                itImagen = mapImg.keySet().iterator();
                                                while (itImagen.hasNext()) {
                                                    String key = itImagen.next();
                                                    img = mapImg.get(key);
                                                    imagenes.add(img);
                                                }
                                            }
                                        }
                                    }
*/
                                    break;
                                default:
                                    /*if (baseDatos.get(portadaRC) != null)
                                        imagenes = baseDatos.get(portadaRC);
                                    break;*/
                            }
                        }

                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                            if (e.getMessage()==null)
                                showToast(getContext(),R.string.sinCobertura);
                            else
                                showToast(getContext(),R.string.sinCobertura);
                        }
                    });
            }
        }
    }

    public void goToHome() {
        FragmentTransaction transaccion = null;
        Fragment fragment;
        int numMaxCargas;

        numMaxCargas = cargas.size();
        if (getFragmentManager()!=null)
            transaccion = getFragmentManager().beginTransaction();
        else {
            transaccion = getActivity().getSupportFragmentManager().beginTransaction();
        }
        fragment = new HomeViewPager();
        numCarga++;
        hideProgressDialog();
        if (numCarga == numMaxCargas) {
            transaccion.replace(R.id.content_frame, fragment).commit();

        }
    }
}
