package cf.castellon.turistorre.fragments.ActionBar;

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
        void pedirPermiso(String permiso, int permisoRequest, View viewSnack);
        void goCamera(Map<String,Object> tipoBean);
        void goGaleria(Map<String,Object> tipoBean, ImageView terrat);
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
                throw new ClassCastException(mActivity.toString() + " debe implementar OnHeadlineSelectedListener");
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
        if (mFirebaseUser !=null)
            switch (v.getId()) {
                case (R.id.btnEnvTerrat):
                    guardarFotoStorageFire(referenciasFire.get(Tablas.Terrats.name()),getContext(),getFragmentManager(),etDireccion.getText().toString());
                    break;
                case (R.id.btnGalTerrat):
                    if (numPermisos==2)
                        mCallback.goGaleria(referenciasFire.get(Tablas.Terrats.name()),ivTerrat);
                    ivTerrat.setImageBitmap(null);
                    break;
            }
        else
            showWarning(getActivity(),"Registrate para subir tu terrat");
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        Editor editor;

        switch (requestCode) {
            case PERMISO_ESCRIBIR_SD :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    numPermisos = 2;
                else
                    showError(getActivity(),GenerarTerrat.class.getName(),"onRequestPermissionsResult","Permiso denegado. Permisos vigentes " + numPermisos);
                break;
        }
        editor = prefs.edit();
        editor.putInt("numPermisos", numPermisos);
        editor.apply();
    }*/


}
