package cf.castellon.turistorre.fragments;

import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MiPageHomeAdapter;
import cf.castellon.turistorre.utils.FixedSpeedScroller;


public class HomeFragment extends Fragment {
    ViewPager mViewPager;
    MiPageHomeAdapter adaptador;
    MiPageFragment mMiPageFragment;
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
        fragmentManager=getChildFragmentManager();
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
        crearImagenes();
        crearUsuarios();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.home_layout,container,false);

        mViewPager = (ViewPager)inflatedView.findViewById(R.id.vpHome);
        //Para que el scroll automatico no sea tan brusco:
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);

            Interpolator sInterpolator = new DecelerateInterpolator();
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), sInterpolator);
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        adaptador = new MiPageHomeAdapter(fragmentManager);
        crearPaginas();
        mViewPager.setAdapter(adaptador);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return inflatedView;
    }

    private void crearPaginas() {
        String[] titulos;

        titulos = getResources().getStringArray(R.array.titulosHome);
        for (int i = 0; i < titulos.length; i++) {
            mMiPageFragment = new MiPageFragment();
            adaptador.addFragment(mMiPageFragment);
        }
    }
}
