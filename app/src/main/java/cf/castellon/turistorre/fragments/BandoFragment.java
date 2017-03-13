package cf.castellon.turistorre.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MiFireAdapterBandoRecyclerView;
import cf.castellon.turistorre.bean.Bando;

import static cf.castellon.turistorre.utils.Constantes.mDataBaseBandoRef;
import static cf.castellon.turistorre.utils.Utils.mFirebaseUser;


public class BandoFragment extends Fragment {
    private FragmentTransaction transaccion;
    @Bind(R.id.rvBando) RecyclerView rvBando;
    private MiFireAdapterBandoRecyclerView adaptador;
    private LinearLayoutManager manager;
    private FragmentManager fragmentManager;
    private Bundle bund;
    private BandoSeleccionadoFragment bandoSeleccionadoFragment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        adaptador = new MiFireAdapterBandoRecyclerView(Bando.class,R.layout.fila_bando_layout,MiFireAdapterBandoRecyclerView.MiFireViewHolder.class,mDataBaseBandoRef,getContext());
        manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        fragmentManager=getChildFragmentManager();
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bando bando = adaptador.getItem(rvBando.getChildAdapterPosition(v));
                bund = new Bundle();
                bund.putString("TITULO", bando.getTitulo());
                bund.putString("DESCRIPCION", bando.getDescripcion());
                bund.putString("IMAGEN_URL_STR", bando.getUrl());
                bandoSeleccionadoFragment = new BandoSeleccionadoFragment();
                bandoSeleccionadoFragment.setArguments(bund);
                transaccion = getFragmentManager().beginTransaction();
                transaccion.replace(R.id.content_frame, bandoSeleccionadoFragment).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.bando_layout,container,false);
        ButterKnife.bind(this,view);

        rvBando.setHasFixedSize(true);
        rvBando.setLayoutManager(manager);
        rvBando.setAdapter(adaptador);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser!=null)
            switch (item.getItemId()){
                case (R.id.it_bando):
                    transaccion = getFragmentManager().beginTransaction();
                    transaccion.replace(R.id.content_frame,new GenerarBandoFragment()).commit();
            }
        else
            Toast.makeText(getContext(),"Solo usuarios autorizados",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bando,menu);
    }
}
