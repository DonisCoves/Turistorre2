package cf.castellon.turistorre.fragments;

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

public class CarruselGaleriaFragment extends Fragment{
    private ViewPager mViewPager;
    private FragmentPageCarruselAdapter adaptador;
    private CarruselImagenFragment fragmentImagen ;
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

        inflatedView = inflater.inflate(R.layout.home_layout,container,false);

        mViewPager = (ViewPager)inflatedView.findViewById(R.id.vpHome);
        adaptador = new FragmentPageCarruselAdapter(fragmentManager);
        fragmentImagen = new CarruselImagenFragment();
        //Creamos las paginas

        crearPaginasImagenes(bund.getString("UID_IMG"));
        mViewPager.setAdapter(adaptador);
        return inflatedView;
    }

    private void crearPaginasImagenes(String idPrimera) {
        final Imagen imagen = buscarImagen(idPrimera);
        bund = new Bundle();
        bund.putString("URL_IMG", imagen.getUriStr());
        bund.putString("ID_IMG", imagen.getUidImg());
        Usuario usuario = buscarUsuario(imagen.getUidUser());
        bund.putString("AVATAR", usuario.getAvatar());
        fragmentImagen = new CarruselImagenFragment();
        fragmentImagen.setArguments(bund);
        adaptador.addFragment(fragmentImagen);
        for (Imagen mImagen:imagenes) {
            if (!mImagen.getUidImg().equals(imagen.getUidImg())) {  //Esta ya la hemos visto
                bund = new Bundle();
                bund.putString("URL_IMG", mImagen.getUriStr());
                bund.putString("ID_IMG", mImagen.getUidImg());
                Usuario user = buscarUsuario(mImagen.getUidUser());
                bund.putString("AVATAR", user.getAvatar());
                fragmentImagen = new CarruselImagenFragment();
                fragmentImagen.setArguments(bund);
                adaptador.addFragment(fragmentImagen);
            }
        }
    }
}
