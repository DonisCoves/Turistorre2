package cf.castellon.turistorre.Servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.buscarUsuario;
import static cf.castellon.turistorre.utils.Utils.usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.ui.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMMessagingService";
    private String action;
    private String uidUser,grupo;
    private DatabaseReference ref;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            action = remoteMessage.getNotification().getClickAction();
            switch (action) {
                case ACTION_GPO_BANDO:
                    ref = mDataBaseBandoRef.child(remoteMessage.getData().get("uidBando"));
                    ref.addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Imagen bando = dataSnapshot.getValue(Imagen.class);
                                    showBandoNotification(bando, remoteMessage);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Bando:onCancelled", databaseError.toException());
                                }
                            });
                    break;
                case ACTION_CAMBIO_GRUPO:
                    uidUser = remoteMessage.getData().get("uidUser");
                    grupo = remoteMessage.getData().get("grupo");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("¿Conceder Permiso?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Usuario user = buscarUsuario(uidUser);
                                    mDataBaseGruposRef.child(grupo).child(usuario.getUidUser()).setValue(usuario);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                case ACTION_GPO_TERRAT:
                    ref = mDataBaseTerratRef.child(remoteMessage.getData().get("uidTerrat"));
                    ref.addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Imagen pano = dataSnapshot.getValue(Imagen.class);
                                    showTerratNotification(pano, remoteMessage);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Bando:onCancelled", databaseError.toException());
                                }
                            });
                    break;
            }
        }
//        showBasicNotification(titulo);

        //showInboxStyleNotification(message);


        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

    }

    private void showBandoNotification(Imagen bando, RemoteMessage remoteMessage) {
        Intent i = new Intent(this, MainActivity.class);
        i.setAction(ACTION_GPO_BANDO);
        i.putExtra("uidBando", remoteMessage.getData().get("uidBando"));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        int idSound = getResources().getIdentifier(remoteMessage.getNotification().getSound(), "raw", getPackageName());
        Uri sonido = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + idSound);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(bando.getTitulo())
                .setColor(16728193)
                .setContentText(bando.getDescripcion())
                .setSound(sonido)
                .setSmallIcon(R.drawable.ic_stat_touch_app)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void showTerratNotification(Imagen pano, RemoteMessage remoteMessage) {
        Intent i = new Intent(this, MainActivity.class);
        i.setAction(ACTION_GPO_TERRAT);
        i.putExtra("uidTerrat", remoteMessage.getData().get("uidTerrat"));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        int idSound = getResources().getIdentifier(remoteMessage.getNotification().getSound(), "raw", getPackageName());
        Uri sonido = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" + idSound);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Terrat añadido en:")
                .setContentText(pano.getTitulo())
                .setSound(sonido)
                .setSmallIcon(R.drawable.ic_action_ic_terraza)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
