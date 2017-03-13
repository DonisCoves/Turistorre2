package cf.castellon.turistorre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;

/**
 * Created by pccc on 13/03/2017.
 */



public class Permisos extends Fragment {
    private String grupoActual;
    @Bind(R.id.tvPermiso) TextView tvPermiso;
    @Bind(R.id.tvPoder) TextView tvPoder;
    @Bind(R.id.tvNoPoder) TextView tvNoPoder;
    @Bind(R.id.spPermisos) Spinner spiner;
    @Bind(R.id.btEnvPermiso) Button btEnviar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.permisos_layout, container, false);
        ButterKnife.bind(this, view);

        grupoActual = getArguments().getString("GRUPO");
        tvPermiso.setText(grupoActual);
        switch (grupoActual) {
            case "anonimo":
                tvPoder.setText(getContext().getResources().getString(R.string.anonimo));
                tvNoPoder.setText(getContext().getResources().getString(R.string.no_anonimo));
                break;
            case "multimedia":
                tvPoder.setText(getContext().getResources().getString(R.string.multimedia));
                tvNoPoder.setText(getContext().getResources().getString(R.string.no_multimedia));
                break;
            case "bandos":
                tvPoder.setText(getContext().getResources().getString(R.string.bandos));
                tvNoPoder.setText(getContext().getResources().getString(R.string.no_bandos));
                break;
            case "administrador":
                tvPoder.setText(getContext().getResources().getString(R.string.administrador));
                tvNoPoder.setText(getContext().getResources().getString(R.string.no_administrador));
                break;
        }
        return view;
    }
}
