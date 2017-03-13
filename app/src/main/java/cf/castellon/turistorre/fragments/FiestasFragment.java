package cf.castellon.turistorre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterDiasFiestaRecyclerView;
import cf.castellon.turistorre.bean.*;

import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;


public class FiestasFragment extends Fragment {
    private MyFireAdapterDiasFiestaRecyclerView adaptadorDiasFiestas, adaptadorViejoDiasFiestas;
    private ArrayAdapter<String> adaptadorFiestas;
    @Bind(R.id.spFiestas)
    Spinner spFiestas;
    @Bind(R.id.rvFiestasDias)
    RecyclerView recView;
    private String uidFiestaSeleccionada;
    private DatabaseReference mDataBaseDiasFiestaSelRef;
    private Query keysFire;
    private Bundle bund;
    private Eventos eventosFragment;
    private FragmentTransaction transaccion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        List<String> uidsFiestas;

        super.onCreate(savedInstanceState);
        uidsFiestas = getUidsFiestas();

        //Por defecto mostramos la ultima fecha
        uidFiestaSeleccionada = getUltimaFiesta();
        keysFire = mDataBaseKeysRef.child(uidFiestaSeleccionada).orderByValue();
        adaptadorFiestas = new ArrayAdapter<>(getContext(), R.layout.fila_spinner_fiestas, R.id.tvFiestasSpinner, uidsFiestas);
        mDataBaseDiasFiestaSelRef = mDataBaseFiestasRef.child(uidFiestaSeleccionada).child("diasFiestas");
        adaptadorDiasFiestas = new MyFireAdapterDiasFiestaRecyclerView(DiaFiestaMeta.class,
                R.layout.fila_diasfiesta_layout,
                MyFireAdapterDiasFiestaRecyclerView.MyFireViewHolder.class,
                keysFire,
                mDataBaseDiasFiestaSelRef);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayoutManager manager;

        View view = inflater.inflate(R.layout.fiestas_layout, container, false);

        ButterKnife.bind(this, view);
        spFiestas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view;
                TextView textoTv = (TextView) layout.findViewById(R.id.tvFiestasSpinner);
                uidFiestaSeleccionada = textoTv.getText().toString();
                keysFire = mDataBaseKeysRef.child(uidFiestaSeleccionada).orderByValue();
                mDataBaseDiasFiestaSelRef = mDataBaseFiestasRef.child(uidFiestaSeleccionada).child("diasFiestas");
                adaptadorDiasFiestas = new MyFireAdapterDiasFiestaRecyclerView(DiaFiestaMeta.class,
                        R.layout.fila_diasfiesta_layout,
                        MyFireAdapterDiasFiestaRecyclerView.MyFireViewHolder.class,
                        keysFire,
                        mDataBaseDiasFiestaSelRef);
                adaptadorDiasFiestas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DiaFiestaMeta diaFiestaMeta = adaptadorDiasFiestas.getItem(recView.getChildAdapterPosition(v));
                        bund = new Bundle();
                        bund.putString("UID", diaFiestaMeta.getUidDiaFiesta());
                        eventosFragment = new Eventos();
                        eventosFragment.setArguments(bund);
                        transaccion = getFragmentManager().beginTransaction();
                        transaccion.replace(R.id.content_frame, eventosFragment).commit();
                    }
                });
                adaptadorViejoDiasFiestas = (MyFireAdapterDiasFiestaRecyclerView) recView.getAdapter();
                recView.setAdapter(adaptadorDiasFiestas);

                if (adaptadorViejoDiasFiestas != null)
                    adaptadorViejoDiasFiestas.cleanup();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spFiestas.setAdapter(adaptadorFiestas);
        setHasOptionsMenu(true);

        recView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(manager);
        recView.setAdapter(adaptadorDiasFiestas);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (adaptadorDiasFiestas != null) {
            adaptadorDiasFiestas.cleanup();
        }
        super.onDestroyView();
    }

    /***
     * Recoger todos los uidsFiesta y para cada elemento del arrayList creado poner un uidDiaFiesta de la Fiesta
     * Luego tenemos que ver cual es la mas actual
     *
     * @return uidFiestaSeleccionada + reciente
     */
    private String getUltimaFiesta() {
        Map<String, DiaFiestaMeta> diasFiestasAux;
        Map<String, Date> mapFiestaDia = new HashMap<>(); // "uidFiesta:tituloDiaFiestaFormateado"
        Iterator<String> itFiestas = fiestasMap.keySet().iterator();
        Iterator<String> itDiasFiestasAux;
        Iterator<String> itmapFiestaDia;
        Date fechaMasReciente = new Date();
        String uidFiestaMasReciente = "";
        while (itFiestas.hasNext()) {
            String key = itFiestas.next(); //uidFiesta
            Fiestas fiesta = fiestasMap.get(key);
            diasFiestasAux = fiesta.getDiasFiestas();
            itDiasFiestasAux = diasFiestasAux.keySet().iterator();
            String keyDia = itDiasFiestasAux.next();
            DiaFiestaMeta diaFiestaMeta = diasFiestasAux.get(keyDia);
            mapFiestaDia.put(key, formatearDia(diaFiestaMeta.getTituloDiaFiesta(), key));
        }

        itmapFiestaDia = mapFiestaDia.keySet().iterator();
        while (itmapFiestaDia.hasNext()) {
            String key = itmapFiestaDia.next();
            Date fechaI = mapFiestaDia.get(key);
            if (fechaI.after(fechaMasReciente)) {
                uidFiestaMasReciente = key;
                fechaMasReciente = fechaI;
            }
        }
        return uidFiestaMasReciente;
    }

}
