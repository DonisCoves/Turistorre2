package cf.castellon.turistorre.fragments.Principal;

import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
        View view;

        view = inflater.inflate(R.layout.bando_layout,container,false);
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
        userBandos = prefs.getBoolean("userBandos",false);
        final String uidUser = prefs.getString("uidUser","");
        //Si usuario autenticado y no tiene permisos consultamos sus permisos
        if (mFirebaseUser!=null && !userBandos)
            mDataBaseGruposRef.child("bandos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getKey().equals(uidUser)) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("userBandos", true);
                            editor.apply();
                            transaccion = getFragmentManager().beginTransaction();
                            transaccion.replace(R.id.content_frame,new GenerarBando()).commit();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        if (mFirebaseUser!=null && userBandos)
            switch (item.getItemId()){
                case (R.id.it_bando):
                    transaccion = getFragmentManager().beginTransaction();
                    transaccion.replace(R.id.content_frame,new GenerarBando()).commit();
            }
        else
            showWarning(getActivity(),R.string.notPermision);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bando,menu);
    }
}