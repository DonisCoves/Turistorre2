package cf.castellon.turistorre.fragments.ActionBar.Click;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.fragments.Click.GaleriaPagina;
import static cf.castellon.turistorre.utils.Utils.*;

@SuppressWarnings("unchecked")
public class FiestasGaleriaViewPager extends Fragment{
    @BindView(R.id.vpHome) ViewPager mViewPager;
    FragmentManager fragmentManager;
    Imagen imagenSeleccionada;
    List<Imagen> imagenes;
    Bundle bund;
    GaleriaPagina pagina;
    FragmentPageCarruselAdapter adaptador;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getChildFragmentManager();
        imagenSeleccionada = getArguments().getParcelable("imagen");
        imagenes = getArguments().getParcelableArrayList("imagenes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;

        inflatedView = inflater.inflate(R.layout.galeria_viewpager_layout,container,false);
        ButterKnife.bind(this,inflatedView);
        adaptador = new FragmentPageCarruselAdapter(fragmentManager);
        crearPaginasImagenes();
        mViewPager.setAdapter(adaptador);
        return inflatedView;
    }

    private void crearPaginasImagenes() {
        for (Imagen mImagen:imagenes){
            if (mImagen.getUidImg().equals(imagenSeleccionada.getUidImg()))
                mImagen = imagenSeleccionada;
            bund = new Bundle();
            bund.putParcelable("imagen",mImagen);
            usuario = buscarUsuario(mImagen.getUidUser());
            bund.putParcelable("usuario",usuario);
            pagina = new GaleriaPagina();
            pagina.setArguments(bund);
            adaptador.addFragment(pagina);
        }
    }


}