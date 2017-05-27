package cf.castellon.turistorre.fragments.Principal;

import android.os.Bundle;
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
import cf.castellon.turistorre.adaptadores.MyFireAdapterTerratsRecyclerView;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.ActionBar.GenerarTerrat;
import cf.castellon.turistorre.fragments.Click.TerratSeleccionado;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

public class TerratsRecyclerView extends Fragment {
    @BindView(R.id.rvTerrats)
    RecyclerView recView;
    private MyFireAdapterTerratsRecyclerView adaptador;
    private LinearLayoutManager manager;
    private TerratSeleccionado terratSeleccionadoFragment;
    private Bundle bund;
    private FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptador = new MyFireAdapterTerratsRecyclerView(Imagen.class, R.layout.fila_fire_terrat_recycle, MyFireAdapterTerratsRecyclerView.MyFireViewHolder.class, mDataBaseTerratRef);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        view = inflater.inflate(R.layout.terrats_layout, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagen pano = adaptador.getItem(recView.getChildAdapterPosition(v));
                bund = new Bundle();
                bund.putParcelable("imagen", pano);
                Usuario usuario = buscarUsuario(pano.getUidUser());
                bund.putParcelable("usuario", usuario);
                terratSeleccionadoFragment = new TerratSeleccionado();
                terratSeleccionadoFragment.setArguments(bund);
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, terratSeleccionadoFragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        recView.setAdapter(adaptador);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_terrats, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser != null)
            switch (item.getItemId()) {
                case R.id.it_gal_terrats:
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, new GenerarTerrat()).commit();
            }
        else
            showWarning(getActivity(), R.string.notRegister);
        return super.onOptionsItemSelected(item);
    }
}