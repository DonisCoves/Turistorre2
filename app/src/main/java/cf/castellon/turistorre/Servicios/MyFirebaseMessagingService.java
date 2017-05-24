package cf.castellon.turistorre.Servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.buscarTerrat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.ui.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMMessagingService";
    private String action;
    private DatabaseReference ref;
    Map<String,String> datos;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        datos=new HashMap<>();
        datos = remoteMessage.getData();
        // Check if message contains a data payload.
        if (!datos.isEmpty()) {
            action = remoteMessage.getNotification().getClickAction();
            switch (action) {
                case ACTION_GPO_BANDO:
                    showBandoNotification(remoteMessage);
                    break;
                case ACTION_CAMBIO_GRUPO:
                    showCambioGrupoNotification(remoteMessage);
                    break;
                case ACTION_GPO_TERRAT:
                    showTerratNotification(remoteMessage);
                    ref = mDataBaseImgRef.child(remoteMessage.getData().get("uidTerrat"));
                    break;
            }
        }
    }

    private void showBandoNotification(RemoteMessage remoteMessage) {
        Map<String, String> datos;
        Intent i;
        PendingIntent pendingIntent;
        int idSound;
        Uri sonido;
        NotificationCompat.Builder builder;

        datos = remoteMessage.getData();
        i = new Intent(this, MainActivity.class);
        i.setAction(ACTION_GPO_BANDO);
        i.putExtra("uidBando", datos.get("uidBando"));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        idSound = getResources().getIdentifier(remoteMessage.getNotification().getSound(), "raw", getPackageName());
        sonido = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + idSound);

        builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.bando))
                .setColor(16728193)
                .setContentText(datos.get("titulo") + " " + datos.get("fecha"))
                .setSound(sonido)
                .setSmallIcon(R.drawable.ic_stat_touch_app)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void showCambioGrupoNotification(RemoteMessage remoteMessage) {
        Map<String, String> datos ;
        Intent i;
        int idSound;
        PendingIntent pendingIntent;
        Uri sonido;
        NotificationCompat.Builder builder;
        NotificationManager manager;

        datos = remoteMessage.getData();
        i = new Intent(this, MainActivity.class);
        i.setAction(ACTION_CAMBIO_GRUPO);
        i.putExtra("uidUser", datos.get("uidUser"));
        i.putExtra("nombre", datos.get("nombre"));
        i.putExtra("grupoNuevo", datos.get("grupoNuevo"));
        i.putExtra("grupoViejo", datos.get("grupoViejo"));
        i.putExtra("avatar", datos.get("avatar"));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        idSound = getResources().getIdentifier(remoteMessage.getNotification().getSound(), "raw", getPackageName());
        sonido = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + idSound);
        builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Solicitud de permisos para: ")
                .setColor(16728193)
                .setContentText(datos.get("nombre"))
                .setSound(sonido)
                .setSmallIcon(R.drawable.ic_stat_touch_app)
                .setContentIntent(pendingIntent);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void showTerratNotification(RemoteMessage remoteMessage) {
        Map<String, String> datos ;
        Intent i ;
        PendingIntent pendingIntent;
        int idSound;
        Uri sonido;
        NotificationCompat.Builder builder;
        NotificationManager manager;
        Imagen pano;
        String uidTerrat;

        datos = remoteMessage.getData();
        i= new Intent(this, MainActivity.class);
        i.setAction(ACTION_GPO_TERRAT);
        uidTerrat = datos.get("uidTerrat");
        i.putExtra("uidTerrat", uidTerrat);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pano  = buscarTerrat(uidTerrat);
        pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        idSound = getResources().getIdentifier(remoteMessage.getNotification().getSound(), "raw", getPackageName());
        sonido = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + idSound);
        builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Terrat añadido en:")
                .setContentText(pano.getTitulo())
                .setSound(sonido)
                .setSmallIcon(R.drawable.ic_action_terrat)
                .setContentIntent(pendingIntent);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void showBasicNotification(String message) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Basic Notification")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());

    }

    public void showInboxStyleNotification(String message) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Inbox Style notification")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.mipmap.ic_launcher, "show activity", pendingIntent);

        Notification notification = new Notification.InboxStyle(builder)
                .addLine(message).addLine("Second message")
                .addLine("Third message")
                .setSummaryText("+3 more").build();
        // Put the auto cancel notification flag
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
