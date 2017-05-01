package cf.castellon.turistorre.utils;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.ArrayList;
import cf.castellon.turistorre.R;

import static cf.castellon.turistorre.utils.Constantes.REQUEST_CODE_FACEBOOK;
import static cf.castellon.turistorre.utils.Utils.*;
public class MiAplicacion extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //[1.- Facebook]
        FacebookSdk.sdkInitialize(getApplicationContext(),REQUEST_CODE_FACEBOOK);

//        FacebookSdk.sdkInitialize(this);
        //[END 1.- Facebook]
        tokenFireBase = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);        //Persistencia local
        establecerPreferenciasIniciales(getApplicationContext());
        setTheme(R.style.dialogos);
        establecerEstructurasIniciales();
    }


}
