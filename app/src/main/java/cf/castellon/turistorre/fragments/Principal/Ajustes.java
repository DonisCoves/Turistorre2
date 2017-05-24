package cf.castellon.turistorre.fragments.Principal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.google.firebase.messaging.FirebaseMessaging;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import static cf.castellon.turistorre.utils.Constantes.BANDOS;
import static cf.castellon.turistorre.utils.Constantes.CATALAN;
import static cf.castellon.turistorre.utils.Constantes.IMAGENES;
import static cf.castellon.turistorre.utils.Constantes.NINGUNA;
import static cf.castellon.turistorre.utils.Constantes.TERRATS;
import static cf.castellon.turistorre.utils.Constantes.TODAS;
import static cf.castellon.turistorre.utils.Utils.prefs;


public class Ajustes extends Fragment {
    @BindView(R.id.spNotificaciones) AppCompatSpinner spNotificaciones;
    @BindView(R.id.spIdiomas) AppCompatSpinner spIdiomas;
    ArrayAdapter <String> adaptadorNotificaciones;
    private int notificacionesPrefs;
    private int idiomasPrefs;
    private SharedPreferences.Editor editor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptadorNotificaciones = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item);
        notificacionesPrefs = prefs.getInt("notificaciones", TODAS);
        idiomasPrefs = prefs.getInt("idiomas", CATALAN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ajustes_layout,container,false);
        ButterKnife.bind(this,view);

        spNotificaciones.setSelection(notificacionesPrefs);
        spIdiomas.setSelection(idiomasPrefs);

        spNotificaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //notificacionesPrefs = prefs.getInt("notificaciones", TODAS);
                if (position != notificacionesPrefs){
                    editor = prefs.edit();
                    editor.putInt("notificaciones", position);
                    editor.apply();
                    switch (position)  {
                        case TODAS:
                            FirebaseMessaging.getInstance().subscribeToTopic("Bando");
                            FirebaseMessaging.getInstance().subscribeToTopic("Terrat");
                            FirebaseMessaging.getInstance().subscribeToTopic("Imagenes");
                            break;
                        case BANDOS:
                            FirebaseMessaging.getInstance().subscribeToTopic("Bando");
                            break;
                        case TERRATS:
                            FirebaseMessaging.getInstance().subscribeToTopic("Terrat");
                            break;
                        case IMAGENES:
                            FirebaseMessaging.getInstance().subscribeToTopic("Imagenes");
                            break;
                        case NINGUNA:
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Bando");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Terrat");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Imagenes");
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return view;
    }
}
