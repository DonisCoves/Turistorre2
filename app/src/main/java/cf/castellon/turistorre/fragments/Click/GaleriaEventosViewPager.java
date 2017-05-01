package cf.castellon.turistorre.fragments.Click;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.DiaFiesta;
import cf.castellon.turistorre.bean.Evento;
import cf.castellon.turistorre.bean.Imagen;

import static cf.castellon.turistorre.utils.Utils.*;

public class GaleriaEventosViewPager extends Fragment {
    @BindView(R.id.vpHome)
    ViewPager mViewPager;
    private FragmentPageCarruselAdapter adaptador;
    private FragmentManager fragmentManager;
    private Imagen imagenSeleccionada;
    private String uidEvento, uidDiaFiesta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getChildFragmentManager();
        imagenSeleccionada = getArguments().getParcelable("imagen");
        uidEvento = getArguments().getString("uidEvento");
        uidDiaFiesta = getArguments().getString("uidDiaFiesta");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;

        inflatedView = inflater.inflate(R.layout.home_viewpager_layout, container, false);
        ButterKnife.bind(this, inflatedView);
        adaptador = new FragmentPageCarruselAdapter(fragmentManager);
        crearPaginasImagenes();
        mViewPager.setAdapter(adaptador);
        return inflatedView;
    }

    private void crearPaginasImagenes() {
        Map<String,Imagen> imagenesMap;
        DiaFiesta diaFiesta;
        Evento evento;
        Iterator <String> iterator;
        Bundle bund;
        GaleriaPagina pagina;

        diaFiesta = buscarDiaFiesta(uidDiaFiesta);
        evento = buscarEvento(diaFiesta,uidEvento);
        bund = new Bundle();
        bund.putParcelable("imagen", imagenSeleccionada);
        usuario = buscarUsuario(imagenSeleccionada.getUidUser());
        bund.putParcelable("usuario", usuario);
        pagina = new GaleriaPagina();
        pagina.setArguments(bund);
        adaptador.addFragment(pagina);
        imagenesMap = evento.getImagenes();
        iterator = imagenesMap.keySet().iterator();
        while (iterator.hasNext()){
            String uidImagen = iterator.next();
            Imagen mImagen = imagenesMap.get(uidImagen);
            if (!mImagen.getUidImg().equals(imagenSeleccionada.getUidImg())) {
                bund = new Bundle();
                bund.putParcelable("imagen", mImagen);
                usuario = buscarUsuario(mImagen.getUidUser());
                bund.putParcelable("usuario", usuario);
                pagina = new GaleriaPagina();
                pagina.setArguments(bund);
                adaptador.addFragment(pagina);
            }
        }
    }
}