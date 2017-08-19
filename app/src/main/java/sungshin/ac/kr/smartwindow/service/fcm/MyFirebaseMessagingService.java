package sungshin.ac.kr.smartwindow.service.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import sungshin.ac.kr.smartwindow.MainActivity;
import sungshin.ac.kr.smartwindow.PopupActivity;
import sungshin.ac.kr.smartwindow.R;

import static android.R.id.message;

/**
 * Created by Sansung on 2017-06-19.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //추가한것
        String from = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String msg = data.get("message");

        sendNotification(title, msg);
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent_ = new Intent(getApplicationContext(), PopupActivity.class);
        intent_.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent_,
                PendingIntent.FLAG_ONE_SHOT);
//        getApplicationContext().startActivity(intent_);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //아이콘이나 제목 수정 -> 알림의 모양을 설정하는 코드
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
