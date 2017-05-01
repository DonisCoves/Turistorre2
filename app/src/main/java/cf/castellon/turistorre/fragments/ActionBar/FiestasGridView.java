package cf.castellon.turistorre.fragments.ActionBar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.GaleriaDiaAdapter;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.fragments.ActionBar.Click.FiestasGaleriaViewPager;


public class FiestasGridView extends Fragment {
    @BindView(R.id.gvGaleriaDia) GridView gridView;
    private GaleriaDiaAdapter adaptador;
    private ArrayList<Imagen> imagenes; //ArrayList por exigencias del Bundle
    FiestasGaleriaViewPager carruselGaleria;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagenes = getArguments().getParcelableArrayList("imagenes");
        adaptador = new GaleriaDiaAdapter(getActivity(), imagenes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fiestas_gridview_layout, container, false);
        ButterKnife.bind(this, view);
        gridView.setAdapter(adaptador);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Imagen imagen = adaptador.getItem(position);
                Bundle bund =new Bundle();
                bund.putParcelableArrayList("imagenes",imagenes);
                bund.putParcelable("imagen",imagen);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                 carruselGaleria = new FiestasGaleriaViewPager();
                carruselGaleria.setArguments(bund);
                fragmentTransaction.replace(R.id.content_frame, carruselGaleria).commit();
            }
        });
        return view;
    }


}
