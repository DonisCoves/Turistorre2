package cf.castellon.turistorre.pruebas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static cf.castellon.turistorre.utils.Constantes.*;

import cf.castellon.turistorre.R;

public class ActivityFCM extends AppCompatActivity {
    public static final Random RANDOM = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);

//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        String mierda="asdas";
        /*Random random = new Random();
        int messageId = random.nextInt();

        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(ID_REMITENTE + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(RANDOM.nextInt()))
                .addData("mi_mensaje", "Hola Mundo")
                .addData("mi_accion","DECIR_HOLA")
                .build());*/

    }

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
