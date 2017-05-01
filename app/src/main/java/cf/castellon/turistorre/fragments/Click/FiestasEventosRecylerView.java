package cf.castellon.turistorre.fragments.Click;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterEventosRecyclerView;
import cf.castellon.turistorre.bean.DiaFiestaMeta;
import cf.castellon.turistorre.bean.Evento;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.fragments.ActionBar.FiestasGridView;
import cf.castellon.turistorre.fragments.Click.Click.FiestasEventosGaleriaRecyclerView;
import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;

@SuppressWarnings({"ConstantConditions", "unused"})
public class FiestasEventosRecylerView extends Fragment {
    private MyFireAdapterEventosRecyclerView adaptador;
    @BindView(R.id.rvEventos) RecyclerView recView;
    Query keysFire;
    DatabaseReference dataRef;
    DiaFiestaMeta diaFiestaMeta;
    ArrayList<Imagen> imagenes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        diaFiestaMeta = getArguments().getParcelable("diaFiestaMeta");
        keysFire = mDataBaseKeysRef.child(diaFiestaMeta.getUidDiaFiesta()).orderByValue();
        dataRef = mDataBaseDiasFiestaRef.child(diaFiestaMeta.getUidDiaFiesta()).child("eventos");
        imagenes=new ArrayList<>();
        adaptador = new MyFireAdapterEventosRecyclerView(Evento.class,
                                                         R.layout.fila_eventos_layout,
                                                         MyFireAdapterEventosRecyclerView.MyFireViewHolder.class,
                                                         keysFire,
                                                         dataRef);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        LinearLayoutManager manager;

        view = inflater.inflate(R.layout.fiestaseventos_recyclerview_layout, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Evento evento = adaptador.getItem(recView.getChildAdapterPosition(v));
                Bundle bund = new Bundle();
                bund.putString("UIDDia", diaFiestaMeta.getUidDiaFiesta());
                bund.putString("UIDEvento", evento.getUidEvento());
                FiestasEventosGaleriaRecyclerView galeriaEventos = new FiestasEventosGaleriaRecyclerView();
                galeriaEventos.setArguments(bund);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, galeriaEventos).commit();
            }
        });
        recView.setAdapter(adaptador);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dia, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Pinchamos para ver las fotos de todo un dia. No es a tiempo Real
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.it_galeriaDias:
                    DatabaseReference ref = mDataBaseDiasFiestaRef.child(diaFiestaMeta.getUidDiaFiesta()).child("eventos");
                    showProgressDialog(getContext(),"Cargando Imagenes");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot eventoDS: dataSnapshot.getChildren()) {
                                for(DataSnapshot imagenDS: eventoDS.child("imagenes").getChildren()){
                                    Imagen imagen = imagenDS.getValue(Imagen.class);
                                    imagenes.add(imagen);
//                                    anyadirImagenDiaFiesta(uidDiaFiestaMeta,imagen);
                                }
                            }
                            hideProgressDialog();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            FiestasGridView galeriaDiaFragment = new FiestasGridView();
                            Bundle bund = new Bundle();
                            bund.putParcelableArrayList("imagenes",imagenes);
                            galeriaDiaFragment.setArguments(bund);
                            fragmentTransaction.replace(R.id.content_frame, galeriaDiaFragment).commit();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
            }
        return super.onOptionsItemSelected(item);
    }

}