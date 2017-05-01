package cf.castellon.turistorre.fragments.ActionBar;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;

public class GenerarBando extends Fragment {
    @BindView(R.id.etTituloBando) EditText etTitulo;
    @BindView(R.id.etDescBando) EditText etDescripcion;
    @BindView(R.id.ivGenerarBando) ImageView ivBando;
    public AppCompatActivity mActivity;
    OnPedirPermisosListener mCallback;
    public interface OnPedirPermisosListener {
        void pedirPermiso(String permiso, int permisoRequest, View viewSnack, String tabla, ImageView imageView);
        void goCamera(String tipoBean , ImageView bando);
        void goGaleria(String tipoBean, ImageView bando);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generar_bando_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activarServer(getContext());
    }

    @OnClick({R.id.btnConf,R.id.btnMemoria,R.id.btnCamara})
    public void onClick(View v){
        switch (v.getId()) {
            case (R.id.btnConf):
                Confirmacion();
                break;
            case (R.id.btnMemoria):
                if (numPermisos==2)
                    mCallback.goGaleria(Tablas.Bandos.name(),ivBando);
                else
                    mCallback.pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, etTitulo, Tablas.Bandos.name(),ivBando);
                break;
            case (R.id.btnCamara):
                if (numPermisos==2)
                    mCallback.goCamera(Tablas.Bandos.name(),ivBando);
                else
                    mCallback.pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, etTitulo, Tablas.Bandos.name(), ivBando);
                break;
        }
    }

    private void Confirmacion() {
        Map<String, Object> tipoBando;
        Uri fileUri;

        tipoBando = referenciasFire.get(Tablas.Bandos.name());
        fileUri = (Uri)tipoBando.get("fileUri");
        if (!etTitulo.getText().toString().isEmpty() && !etDescripcion.getText().toString().isEmpty() && fileUri!=null)
            guardarFotoStorageFire(tipoBando,getContext(),getFragmentManager(),etTitulo.getText().toString(),etDescripcion.getText().toString());
         else
            showWarning(getContext(),"Rellena el título y la descripcion y pon una imagen");
    }
}
