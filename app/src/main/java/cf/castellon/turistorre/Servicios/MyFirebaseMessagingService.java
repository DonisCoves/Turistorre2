/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cf.castellon.turistorre.Servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import static cf.castellon.turistorre.utils.Constantes.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Panoramica;
import cf.castellon.turistorre.ui.MainActivity;
import cf.castellon.turistorre.bean.Bando;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMMessagingService";
    private String action;
    private String titulo;
    private String uidBando;
    private Bando bando;
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
                                    Bando bando = dataSnapshot.getValue(Bando.class);
                                    showBandoNotification(bando,remoteMessage);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Bando:onCancelled", databaseError.toException());
                                }
                            });
                case ACTION_GPO_TERRAT:
                    ref = mDataBaseTerratsRef.child(remoteMessage.getData().get("uidTerrat"));
                    ref.addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Panoramica pano = dataSnapshot.getValue(Panoramica.class);
                                    showTerratNotification(pano,remoteMessage);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Bando:onCancelled", databaseError.toException());
                                }
                            });
            }
        }
//        showBasicNotification(titulo);

        //showInboxStyleNotification(message);


        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

    }

    private void showBandoNotification(Bando bando, RemoteMessage remoteMessage) {
        Intent i = new Intent(this,MainActivity.class);
        i.setAction(ACTION_GPO_BANDO);
        i.putExtra("uidBando",remoteMessage.getData().get("uidBando"));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        int idSound = getResources().getIdentifier(remoteMessage.getNotification().getSound(),"raw",getPackageName());
        Uri sonido =  Uri.parse("android.resource://"+ getBaseContext().getPackageName() + "/" + idSound);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(bando.getTitulo())
                .setContentText(bando.getDescripcion())
                .setSound(sonido)
                .setSmallIcon(R.mipmap.ic_action_bando)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }

    private void showTerratNotification(Panoramica pano, RemoteMessage remoteMessage) {
        Intent i = new Intent(this,MainActivity.class);
        i.setAction(ACTION_GPO_TERRAT);
        i.putExtra("uidTerrat",remoteMessage.getData().get("uidTerrat"));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        int idSound = getResources().getIdentifier(remoteMessage.getNotification().getSound(),"raw",getPackageName());
        Uri sonido =  Uri.parse("android.resource://"+ getBaseContext().getPackageName() + "/" + idSound);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Terrat añadido en:")
                .setContentText(pano.getTitulo())
                .setSound(sonido)
                .setSmallIcon(R.mipmap.ic_action_ic_terraza)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }

    private void showBasicNotification(String message) {
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Basic Notification")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());

    }

    public void showInboxStyleNotification(String message) {
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

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
