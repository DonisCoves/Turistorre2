package cf.castellon.turistorre.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;

import static cf.castellon.turistorre.utils.Constantes.CAPTURE_IMAGE_GALLERY_ACTIVITY_REQUEST_CODE;
import static cf.castellon.turistorre.utils.Utils.eliminarGrupoActual;
import static cf.castellon.turistorre.utils.Utils.generarNotificacionAdminCambioGrupo;
import static cf.castellon.turistorre.utils.Utils.generarNotificacionBando;
import static cf.castellon.turistorre.utils.Utils.usuario;

/**
 * Created by pccc on 13/03/2017.
 */



public class Permisos extends Fragment {
    private String grupoActual;
    @Bind(R.id.tvPermiso) TextView tvPermiso;
    @Bind(R.id.tvPoder) TextView tvPoder;
    @Bind(R.id.tvNoPoder) TextView tvNoPoder;
    @Bind(R.id.spPermisos) Spinner spiner;
    private List<String> grupos;
    private ArrayAdapter<String> adaptador;
    private String gpoSolicitado;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grupoActual = getArguments().getString("GRUPO");
        grupos = Arrays.asList(getContext().getResources().getStringArray(R.array.grupos));
        grupos = eliminarGrupoActual(grupos,grupoActual);
        adaptador = new ArrayAdapter<>(getContext(), R.layout.fila_spinner_permisos, R.id.tvPermisosSpinner, grupos);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.permisos_layout, container, false);
        ButterKnife.bind(this, view);

        tvPermiso.setText(grupoActual);
        tvPoder.setText(getContext().getResources().getIdentifier(grupoActual,"string",getContext().getPackageName()));
        tvNoPoder.setText(getContext().getResources().getIdentifier("no_"+grupoActual,"string",getContext().getPackageName()));
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) view;
                TextView textoTv = (TextView) layout.findViewById(R.id.tvPermisosSpinner);
                gpoSolicitado = textoTv.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spiner.setAdapter(adaptador);
     return view;
    }

    @OnClick(R.id.btEnvPermiso)
    public void onClick(View v){
        generarNotificacionAdminCambioGrupo(gpoSolicitado,usuario);
    }

}
