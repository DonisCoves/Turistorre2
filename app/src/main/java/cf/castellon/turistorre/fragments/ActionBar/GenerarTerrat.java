package cf.castellon.turistorre.fragments.ActionBar;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.ui.MainActivity;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

public class GenerarTerrat extends Fragment {
    @BindView(R.id.etDirTerrat) EditText etDireccion;
    @BindView(R.id.ivGenTerrat) ImageView ivTerrat;
    public AppCompatActivity mActivity;
    OnPedirPermisosListener mCallback;
    public interface OnPedirPermisosListener {
        void pedirPermiso(String permiso, int permisoRequest, View viewSnack, String tabla, ImageView imageView);
        void goGaleria(String tipoBean, ImageView terrat);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generar_terrat_layout,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
            try {
                mCallback = (OnPedirPermisosListener) mActivity;
            } catch (ClassCastException e) {
                throw new ClassCastException(mActivity.toString() + " debe implementar OnPedirPermisosListener");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        activarServer(getContext());
    }

    @OnClick({R.id.btnGalTerrat,R.id.btnEnvTerrat,R.id.btnCanTerrat})
    public void onClick(View v){
            switch (v.getId()) {
                case (R.id.btnEnvTerrat):
                    guardarFotoStorageFire(referenciasFire.get(Tablas.Terrats.name()),getContext(),getFragmentManager(),etDireccion.getText().toString(),null);
                    break;
                case (R.id.btnGalTerrat):
                    if (numPermisos==2)
                        mCallback.goGaleria(Tablas.Terrats.name(),ivTerrat);
                    else
                        mCallback.pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, ivTerrat, Tablas.Terrats.name(), ivTerrat);
                    ivTerrat.setImageBitmap(null);
                    break;
            }
    }
}
