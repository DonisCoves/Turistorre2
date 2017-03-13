package cf.castellon.turistorre.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;


public class AjustesFragment extends Fragment {
    @Bind(R.id.spNotificaciones) AppCompatSpinner spNotificaciones;
    @Bind(R.id.spIdiomas) AppCompatSpinner spIdiomas;
    private ArrayAdapter <String> adaptadorNotificaciones;
    private ArrayAdapter <String> adaptadorIdiomas;
    private int notificacionesPrefs;
    private int idiomasPrefs;
    private SharedPreferences.Editor editor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptadorNotificaciones = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ajustes_layout,container,false);
        ButterKnife.bind(this,view);

        notificacionesPrefs = prefs.getInt("notificaciones", TODAS);
        idiomasPrefs = prefs.getInt("idiomas", CATALAN);

        spNotificaciones.setSelection(notificacionesPrefs);
        spIdiomas.setSelection(idiomasPrefs);

        spNotificaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                notificacionesPrefs = prefs.getInt("notificaciones", TODAS);
                if (position != notificacionesPrefs){
                    editor = prefs.edit();
                    editor.putInt("notificaciones", position);
                    editor.commit();
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
       /* spNotificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mierda="mierda";
            }
        });*/
        return view;
    }
    public void configurarNotificacion() {

    }
}
