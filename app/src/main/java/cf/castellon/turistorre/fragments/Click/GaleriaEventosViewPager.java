package cf.castellon.turistorre.fragments.Click;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;

import static cf.castellon.turistorre.utils.Utils.*;

public class GaleriaEventosViewPager extends Fragment{
    private ViewPager mViewPager;
    private FragmentPageCarruselAdapter adaptador;
    private GaleriaPagina fragmentImagen ;
    private FragmentManager fragmentManager;
    private Bundle bund;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getChildFragmentManager();
        bund = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;

        inflatedView = inflater.inflate(R.layout.home_viewpager_layout,container,false);

        mViewPager = (ViewPager)inflatedView.findViewById(R.id.vpHome);
        adaptador = new FragmentPageCarruselAdapter(fragmentManager);
        fragmentImagen = new GaleriaPagina();
        //Creamos las paginas

        crearPaginasImagenes(bund.getString("UID_IMG"));
        mViewPager.setAdapter(adaptador);
        return inflatedView;
    }

    private void crearPaginasImagenes(String idPrimera) {
        /*final Imagen imagen = buscarImagen(idPrimera);
        bund = new Bundle();
        Usuario usuario = buscarUsuario(imagen.getUidUser());
        bund.putParcelable("user", usuario);
        bund.putParcelable("imagen", imagen);
        fragmentImagen = new GaleriaPagina();
        fragmentImagen.setArguments(bund);
        adaptador.addFragment(fragmentImagen);
        for (Imagen mImagen:imagenes) {
            if (!mImagen.getUidImg().equals(imagen.getUidImg())) {  //Esta ya la hemos visto
                bund = new Bundle();
                Usuario user = buscarUsuario(mImagen.getUidUser());
                bund.putParcelable("user", usuario);
                bund.putParcelable("imagen", imagen);
                fragmentImagen = new GaleriaPagina();
                fragmentImagen.setArguments(bund);
                adaptador.addFragment(fragmentImagen);
            }
        }*/
    }
}
