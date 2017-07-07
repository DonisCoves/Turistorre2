package cf.castellon.turistorre.fragments.ActionBar;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
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

public class GenerarRaco extends Fragment {
    @BindView(R.id.etTituloRaco) EditText etTitulo;
    @BindView(R.id.etDescRaco) EditText etDescripcion;
    @BindView(R.id.ivGenerarRaco) ImageView ivRaco;
    OnPedirPermisosListener mCallback;
    AppCompatActivity mActivity;

    public interface OnPedirPermisosListener {
        void pedirPermiso(String permiso, int permisoRequest, View viewSnack, String tabla,ImageView imageView, boolean camara);
        void goCamera(String tipoBean, ImageView view);
        void goGaleria(String tipoBean, ImageView view);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generar_raco_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.btnCamaraRaco,R.id.btnMemoriaRaco,R.id.btnConfRaco,R.id.btnCancRaco})
    public void onClick(View v){
        switch (v.getId()) {
            case (R.id.btnConfRaco):
                confirmacion();
                break;
            case (R.id.btnCancRaco):
                etTitulo.setText("");
                etDescripcion.setText("");
                ivRaco.setImageBitmap(null);
                break;
            case (R.id.btnMemoriaRaco):
                if (numPermisos==2)
                    mCallback.goGaleria(Tablas.Racons.name(),ivRaco);
                else
                    mCallback.pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, etTitulo, Tablas.Racons.name(), ivRaco, false);
                break;
            case (R.id.btnCamaraRaco):
                if (numPermisos==2)
                    mCallback.goCamera(Tablas.Racons.name(),ivRaco);
                else
                    mCallback.pedirPermiso(Manifest.permission.CAMERA, PERMISO_CAMARA, etTitulo, Tablas.Racons.name(), ivRaco, true);
                break;
        }
    }

    private void confirmacion() {
        Map<String, Object> tipoRaco;
        Uri fileUri;

        tipoRaco = referenciasFire.get(Tablas.Racons.name());
        fileUri = (Uri)tipoRaco.get("fileUri");
        if (!etTitulo.getText().toString().isEmpty() && !etDescripcion.getText().toString().isEmpty() && fileUri!=null)
            guardarFotoStorageFire(tipoRaco,getContext(),getFragmentManager(),etTitulo.getText().toString(),etDescripcion.getText().toString());
         else
            showWarning(getContext(),R.string.rellena);
        }
}
