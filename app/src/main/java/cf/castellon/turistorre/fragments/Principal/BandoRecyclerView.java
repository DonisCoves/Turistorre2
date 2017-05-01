package cf.castellon.turistorre.fragments.Principal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MiFireAdapterBandoRecyclerView;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.fragments.Click.BandoSeleccionado;
import cf.castellon.turistorre.fragments.ActionBar.GenerarBando;
import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

public class BandoRecyclerView extends Fragment {
    private FragmentTransaction transaccion;
    @BindView(R.id.rvBando) RecyclerView rvBando;
    private MiFireAdapterBandoRecyclerView adaptador;
    private LinearLayoutManager manager;
    private Bundle bund;
    private BandoSeleccionado bandoSeleccionadoFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        adaptador = new MiFireAdapterBandoRecyclerView(Imagen.class,R.layout.fila_bando_layout,MiFireAdapterBandoRecyclerView.MiFireViewHolder.class,mDataBaseBandoRef);
        manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.bando_layout,container,false);
        ButterKnife.bind(this,view);

        rvBando.setHasFixedSize(true);
        rvBando.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagen bando = adaptador.getItem(rvBando.getChildAdapterPosition(v));
                transaccion = getFragmentManager().beginTransaction();
                bandoSeleccionadoFragment = new BandoSeleccionado();
                bund = new Bundle();
                bund.putParcelable("bando", bando);
                bandoSeleccionadoFragment.setArguments(bund);
                transaccion.replace(R.id.content_frame, bandoSeleccionadoFragment).commit();
            }
        });
        rvBando.setAdapter(adaptador);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser!=null)
            switch (item.getItemId()){
                case (R.id.it_bando):
                    transaccion = getFragmentManager().beginTransaction();
                    transaccion.replace(R.id.content_frame,new GenerarBando()).commit();
            }
        else
            showWarning(getActivity(),"Usuario sin privilegios");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bando,menu);
    }
}