package cf.castellon.turistorre.fragments.Click;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.Imagen;
import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

@SuppressWarnings("unchecked")
public class GaleriaViewPager extends Fragment{
    @BindView(R.id.vpHome) ViewPager mViewPager;
    private FragmentManager fragmentManager;
    private Imagen imagen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getChildFragmentManager();
        imagen = getArguments().getParcelable("imagen");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;
        FragmentPageCarruselAdapter adaptador;

        inflatedView = inflater.inflate(R.layout.galeria_viewpager_layout,container,false);
        ButterKnife.bind(this,inflatedView);
        adaptador = new FragmentPageCarruselAdapter(fragmentManager);
        crearPaginasImagenes(imagen,baseDatos.get(Tablas.Imagenes.name()),adaptador);
        mViewPager.setAdapter(adaptador);
        return inflatedView;
    }


}