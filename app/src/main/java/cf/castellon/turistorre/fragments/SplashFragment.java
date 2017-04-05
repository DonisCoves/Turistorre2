package cf.castellon.turistorre.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.DiaFiesta;
import cf.castellon.turistorre.bean.Fiestas;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;


public class SplashFragment extends Fragment {
    private CargaAsync carga;
    private FragmentTransaction transaccion;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        transaccion = getFragmentManager().beginTransaction();
        Fragment fragment = new FiestasFragment();

        tiempoInicialWeb = System.currentTimeMillis();
        carga = new CargaAsync(fragment);
        carga.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acerca_layout,container,false);
        return view;
    }

    public class CargaAsync extends AsyncTask<Void, Void, Void> {
        Fragment fragment;
        ProgressBar prog;
        ProgressDialog dialogoBarraProgreso;

        public CargaAsync(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog(getContext(),"Cargando Datos");
        }

        @Override
        protected Void doInBackground(Void... params) {
            cargarDatosFiestas(fragment);
            return null;
        }

        @Override
        protected void onPostExecute(Void noUsado) {
        }
    }

        private void cargarDatosFiestas(final Fragment fragment) {
            //Fiestas
            mDataBaseFiestasRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot fiestasList: dataSnapshot.getChildren()) {
                        Fiestas fiestas = fiestasList.getValue(Fiestas.class);
                        fiestasMap.put(fiestasList.getKey(), fiestas);
                    }
                    tiempoDatosRecogidos = System.currentTimeMillis();
                    tiempoEspera = (tiempoDatosRecogidos - tiempoInicialWeb)/100;//3532
                    System.out.println("El tiempo de espera para recoger los datos de fiestas es de : "+tiempoEspera+ " decimas de segundos");
                    hideProgressDialog();
                    transaccion.replace(R.id.content_frame, fragment).commit();
                }
                @Override
                public void onCancelled(DatabaseError arg0) {

                }
            });

/*            //DiasDeFiestas
            mDataBaseDiasFiestaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot diasFiestasList: dataSnapshot.getChildren()) {
                        DiaFiesta diaFiesta = diasFiestasList.getValue(DiaFiesta.class);
                        diasFiestas.put(diasFiestasList.getKey(), diaFiesta);
                    }
                    tiempoDatosRecogidos = System.currentTimeMillis();
                    tiempoEspera = (tiempoDatosRecogidos - tiempoInicialWeb)/100;//3532
                    System.out.println("El tiempo de espera para recoger los datos de diasfiestas es de : "+tiempoEspera+ " decimas de segundos");
                    hideProgressDialog();
                    transaccion.replace(R.id.content_frame, fragment).commit();
                }
                @Override
                public void onCancelled(DatabaseError arg0) {}
            });*/



        }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item ;

        inflater.inflate(R.menu.menu_galeria,menu);
        item = menu.findItem(R.id.itShared);
    }



}
