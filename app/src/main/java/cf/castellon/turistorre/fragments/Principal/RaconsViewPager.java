package cf.castellon.turistorre.fragments.Principal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.fragments.Click.RaconsPagina;
import cf.castellon.turistorre.fragments.ActionBar.GenerarRaco;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Utils.baseDatos;

@SuppressWarnings("unchecked")
public class RaconsViewPager extends Fragment {
    private FragmentPageCarruselAdapter adaptador;
    @BindView(R.id.vpRacons) ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        HashSet racons;

        view = inflater.inflate(R.layout.racons_viewpager_layout,container,false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        adaptador = new FragmentPageCarruselAdapter(getChildFragmentManager());
        racons = baseDatos.get(Tablas.Racons.name());
        crearPaginasRacons(racons);
        viewPager.setAdapter(adaptador);
        return view;
    }

    private void crearPaginasRacons(HashSet<Imagen> racons) {
        RaconsPagina pagina;

        for (Imagen raco:racons){
            Bundle bundle = new Bundle();
            bundle.putParcelable("raco", raco);

            pagina = new RaconsPagina();
            pagina.setArguments(bundle);
            adaptador.addFragment(pagina);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_racons, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction;

        if (mFirebaseUser !=null)
            switch (item.getItemId()){
                case R.id.it_gen_raco:
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,new GenerarRaco()).commit();
                    break;
            }
        else
            showWarning(getContext(),R.string.notPermision);
        return super.onOptionsItemSelected(item);
    }
}