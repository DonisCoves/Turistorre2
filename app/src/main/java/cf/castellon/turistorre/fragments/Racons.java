package cf.castellon.turistorre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.Raco;
import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;

public class Racons extends Fragment {
    private FragmentPageCarruselAdapter adaptador;
    private FragmentTransaction fragmentTransaction;
    @Bind(R.id.vpRacons) ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carrusel_racons_layout,container,false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        adaptador = new FragmentPageCarruselAdapter(getChildFragmentManager());
        crearPaginasRacons(racons);
        viewPager.setAdapter(adaptador);
        return view;
    }

    private void crearPaginasRacons(List<Raco> lRacons) {
        for (int i=lRacons.size()-1;i>=0;i--){
            Bundle bundle = new Bundle();
            bundle.putParcelable("raco", lRacons.get(i));
            CarruselRaco fragmentRaco = new CarruselRaco();
            fragmentRaco.setArguments(bundle);
            adaptador.addFragment(fragmentRaco);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_racons, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser !=null)
            switch (item.getItemId()){
                case R.id.it_gen_raco:
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,new GenerarRaco()).commit();
            }
        else
            Toast.makeText(getContext(),"Usuario sin permisos",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}