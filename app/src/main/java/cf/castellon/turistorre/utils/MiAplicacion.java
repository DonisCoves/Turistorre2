package cf.castellon.turistorre.utils;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import cf.castellon.turistorre.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


import static cf.castellon.turistorre.utils.Constantes.REQUEST_CODE_FACEBOOK;
import static cf.castellon.turistorre.utils.Utils.*;
public class MiAplicacion extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext(),REQUEST_CODE_FACEBOOK);
        tokenFireBase = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);        //Persistencia local
        establecerPreferenciasIniciales(getApplicationContext());
        setTheme(R.style.dialogos);
        establecerEstructurasIniciales();
        loaders(getBaseContext());
    }


}
