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
        showProgressDialog(getContext(), getString(R.string.cargando));
        //cargas.add(CARGAR_FIESTAS);
        //cargas.add(CARGAR_DIAFIESTA);
        cargas.add(CARGAR_HOME);
        cargarDatos(cargas);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    private void cargarDatos(List<Integer> cargas) {
        final HashSet<Fiestas> fiestasHash;
        final HashSet<DiaFiesta> diaFiestasHash;

        fiestasHash = new HashSet<>();
        diaFiestasHash = new HashSet<>();
        for (int carga : cargas) {
            switch (carga) {
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
