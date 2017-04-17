package cf.castellon.turistorre.fragments.Principal;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import static cf.castellon.turistorre.utils.Utils.*;
import java.lang.reflect.Field;
import java.util.HashSet;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MiPageHomeAdapter;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.Click.HomePagina;

import cf.castellon.turistorre.utils.FixedSpeedScroller;

@SuppressWarnings("unchecked")
public class HomeViewPager extends Fragment {
    ViewPager mViewPager;
    MiPageHomeAdapter adaptador;
    HomePagina mPaginaHome;
    FragmentManager fragmentManager;
    private Handler handler;
    private int delay = 5000; //milliseconds
    private int page = 0;
    Runnable runnable = new Runnable() {
        public void run() {
            if (adaptador.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            mViewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        fragmentManager = getChildFragmentManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.home_viewpager_layout, container, false);

        mViewPager = (ViewPager) inflatedView.findViewById(R.id.vpHome);
        //Para que el scroll automatico no sea tan brusco:
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);

            Interpolator sInterpolator = new DecelerateInterpolator();
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), sInterpolator);
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException | IllegalAccessException eNoSuchFieldException) {
            showError(getContext(),getClass().getName(),eNoSuchFieldException.getStackTrace()[0].getMethodName(),eNoSuchFieldException.toString());
        }
        adaptador = new MiPageHomeAdapter(fragmentManager);
        crearPaginas();
        mViewPager.setAdapter(adaptador);
        return inflatedView;
    }

    private void crearPaginas() {
        HashSet<Imagen> imagenes;
//        HashSet<Fiestas> fiestasList;
//        HashSet<DiaFiesta> diaFiestaList;

        Usuario user;

        imagenes = new HashSet<>();
        switch (mFirebaseRemoteConfig.getString(portadaRC)) {
            case ("Usuarios"):
                user = buscarUsuario(mFirebaseRemoteConfig.getString(usuarioUidRC));
                imagenes = buscarImagenes(user.getUidUser());
                break;
            case ("Fiestas"):
//                fiestasList = baseDatos.get(portadaRC);
                break;
            case ("DiasFiestas"):
//                diaFiestaList = baseDatos.get(portadaRC);
                break;
            default: //Terrats,Racons,Imagenes
                imagenes = baseDatos.get(portadaRC);
                break;
            }

        for (Imagen imagen : imagenes) {
            Bundle bundle = new Bundle();
            user = buscarUsuario(imagen.getUidUser());
            bundle.putParcelable("imagen", imagen);
            bundle.putParcelable("usuario", user);
            mPaginaHome = new HomePagina();
            mPaginaHome.setArguments(bundle);
            adaptador.addFragment(mPaginaHome);
        }
    }
}
