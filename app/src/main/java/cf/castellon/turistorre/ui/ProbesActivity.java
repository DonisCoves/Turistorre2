package cf.castellon.turistorre.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterEventosRecyclerView;
import cf.castellon.turistorre.adaptadores.MyFireAdapterEventosRecyclerView2;
import cf.castellon.turistorre.bean.Evento;

import static cf.castellon.turistorre.utils.Constantes.mDataBaseDiasFiestaRef;
import static cf.castellon.turistorre.utils.Constantes.mDataBaseKeysRef;

public class ProbesActivity extends AppCompatActivity {

    private String uidDiaFiestaMeta;
    private MyFireAdapterEventosRecyclerView2 adaptador;
    private LinearLayoutManager manager;
    @Bind(R.id.rvEventos)
    RecyclerView recView;
    private Query keysFire;
    private DatabaseReference ref;
    String parametro1;
    long parametro2;
    boolean parametro3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventos_layout);
        ButterKnife.bind(this);
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        parametro1 = mFirebaseRemoteConfig.getString("Parametro1");
        parametro2 = mFirebaseRemoteConfig.getLong("Parametro2");
        parametro3 = mFirebaseRemoteConfig.getBoolean("Parametro3");
        mFirebaseRemoteConfig.fetch().addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFirebaseRemoteConfig.activateFetched();
            }
        });
        mFirebaseRemoteConfig.activateFetched();

    }

}