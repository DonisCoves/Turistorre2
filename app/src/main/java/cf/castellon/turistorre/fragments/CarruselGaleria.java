package cf.castellon.turistorre.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.ImagenParcel;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.bean.UsuarioParcel;

import static cf.castellon.turistorre.utils.Utils.buscarImagen;
import static cf.castellon.turistorre.utils.Utils.buscarUsuario;
import static cf.castellon.turistorre.utils.Utils.buscarUsuarioParcel;
import static cf.castellon.turistorre.utils.Utils.imagenes;

public class CarruselGaleria extends Fragment{
    private ViewPager mViewPager;
    private FragmentPageCarruselAdapter adaptador;
    private FragmentManager fragmentManager;
    private int posImagenActiva;
    private List<ImagenParcel> imagenes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getChildFragmentManager();
        posImagenActiva = getArguments().getInt("SELECCIONADO");
        imagenes = getArguments().getParcelableArrayList("imagenes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;

        inflatedView = inflater.inflate(R.layout.carrusel_layout,container,false);

        mViewPager = (ViewPager)inflatedView.findViewById(R.id.vpHome);
        adaptador = new FragmentPageCarruselAdapter(fragmentManager);
        //Creamos las paginas
        crearPaginasImagenes(posImagenActiva,imagenes);
        mViewPager.setAdapter(adaptador);
        return inflatedView;
    }

    private void crearPaginasImagenes(int seleccionada, List<ImagenParcel> imagenes) {
        final ImagenParcel imagen = imagenes.get(seleccionada);
        //final Imagen imagen = buscarImagen(idPrimera);
        Bundle bund = new Bundle();
        bund.putParcelable("imagen",imagen);
        UsuarioParcel usuario = buscarUsuarioParcel(imagen.getUidUser());
        bund.putParcelable("usuario",usuario);
        CarruselImagen fragmentImagen = new CarruselImagen();
        fragmentImagen.setArguments(bund);
        adaptador.addFragment(fragmentImagen);
        for (ImagenParcel mImagen:imagenes) {
            if (!mImagen.getUidImg().equals(imagen.getUidImg())) {  //Esta ya la hemos visto
                Bundle bund2 = new Bundle();
                bund2.putParcelable("imagen",mImagen);
                UsuarioParcel usuario2 = buscarUsuarioParcel(mImagen.getUidUser());
                bund2.putParcelable("usuario",usuario2);
                CarruselImagen fragmentImagen2 = new CarruselImagen();
                fragmentImagen2.setArguments(bund2);
                adaptador.addFragment(fragmentImagen2);
            }
        }
    }
}
