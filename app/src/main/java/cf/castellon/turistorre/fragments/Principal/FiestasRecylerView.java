package cf.castellon.turistorre.fragments.Principal;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterDiasFiestaRecyclerView;
import cf.castellon.turistorre.bean.*;
import cf.castellon.turistorre.fragments.Click.FiestasEventosRecylerView;

import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;


@SuppressWarnings("unchecked")
public class FiestasRecylerView extends Fragment {
    private MyFireAdapterDiasFiestaRecyclerView adaptadorDiasFiestas, adaptadorViejoDiasFiestas;
    private ArrayAdapter<String> adaptadorFiestas;
    @BindView(R.id.spFiestas) Spinner spFiestas;
    @BindView(R.id.rvFiestasDias) RecyclerView recView;
    private String uidFiestaSeleccionada;
    private DatabaseReference mDataBaseDiasFiestaSelRef;
    private Query keysFire;
    private Bundle bund;
    private FiestasEventosRecylerView eventosFragment;
    private FragmentTransaction transaccion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        List<String> titulosFiestas;

        super.onCreate(savedInstanceState);
        titulosFiestas = getTitulosFiestas();

        //Por defecto mostramos la ultima fecha
        if (titulosFiestas!=null) {
            uidFiestaSeleccionada = getUltimaFiesta();
            keysFire = mDataBaseKeysRef.child(uidFiestaSeleccionada).orderByValue();
            adaptadorFiestas = new ArrayAdapter<>(getContext(), R.layout.fila_spinner_fiestas, R.id.tvFiestasSpinner, titulosFiestas);
            mDataBaseDiasFiestaSelRef = mDataBaseFiestasRef.child(uidFiestaSeleccionada).child("diasFiestas");
            adaptadorDiasFiestas = new MyFireAdapterDiasFiestaRecyclerView(DiaFiestaMeta.class,
                    R.layout.fila_diasfiesta_layout,
                    MyFireAdapterDiasFiestaRecyclerView.MyFireViewHolder.class,
                    keysFire,
                    mDataBaseDiasFiestaSelRef);

        }
        else
            showError(getContext(),getClass().getName(),"Oncreate","uidsFiestas es null");
        }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayoutManager manager;

        View view = inflater.inflate(R.layout.fiestas_recyclerview_layout, container, false);

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
                        eventosFragment = new FiestasEventosRecylerView();
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

}
