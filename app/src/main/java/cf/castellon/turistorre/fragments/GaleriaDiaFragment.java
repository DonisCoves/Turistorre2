package cf.castellon.turistorre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.GaleriaDiaAdapter;
import cf.castellon.turistorre.bean.ImagenParcel;

/**
 * Created by pccc on 04/03/2017.
 */

public class GaleriaDiaFragment extends Fragment {
    @Bind(R.id.gvGaleriaDia) GridView gridView;
    private GaleriaDiaAdapter adaptador;
    private ArrayList<ImagenParcel> imagenes; //ArrayList por exigencias del Bundle

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagenes = getArguments().getParcelableArrayList("imagenes");
        adaptador = new GaleriaDiaAdapter(getActivity(), imagenes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.galeria_dia, container, false);
        ButterKnife.bind(this, view);
        gridView.setAdapter(adaptador);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Bundle bund =new Bundle();
                bund.putParcelableArrayList("imagenes",imagenes);
                bund.putInt("SELECCIONADO",position);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                CarruselGaleria carruselGaleria = new CarruselGaleria();
                carruselGaleria .setArguments(bund);
                fragmentTransaction.replace(R.id.content_frame, carruselGaleria).commit();

            }
        });
        return view;
    }


}
