package cf.castellon.turistorre.fragments.ActionBar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;
import static cf.castellon.turistorre.utils.Utils.eliminarGrupoActual;
import static cf.castellon.turistorre.utils.Utils.generarNotificacionAdminCambioGrupo;
import static cf.castellon.turistorre.utils.Utils.usuario;

public class Permisos extends Fragment {
    private String grupoActual;
    @BindView(R.id.tvPermiso) TextView tvPermiso;
    @BindView(R.id.tvPoder) TextView tvPoder;
    @BindView(R.id.tvNoPoder) TextView tvNoPoder;
    @BindView(R.id.spPermisos) Spinner spiner;
    List<String> grupos;
    private ArrayAdapter<String> adaptador;
    private String gpoSolicitado;
    int posGpo;

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
                posGpo = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spiner.setAdapter(adaptador);
     return view;
    }

    @OnClick(R.id.btEnvPermiso)
    public void onClick(){
        if (posGpo!=0)
            generarNotificacionAdminCambioGrupo(gpoSolicitado,usuario);
    }
}
