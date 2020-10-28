package techasyluminfo.note.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import techasyluminfo.note.R;
import techasyluminfo.note.ui.MainActivity;

public class AlertReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent intent) {

        String Title = intent.getStringExtra(mContext.getString(R.string.alert_title));
        String content = intent.getStringExtra(mContext.getString(R.string.alert_content));
        String channelId = mContext.getResources().getString(R.string.channel_id);
        CharSequence name = mContext.getResources().getString(R.string.channel_name);
        String description = mContext.getResources().getString(R.string.channel_description);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext.getApplicationContext(), channelId);
        Intent ii = new Intent(mContext.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(content);
        bigText.setBigContentTitle(Title);
        bigText.setSummaryText(content);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_fav);
        mBuilder.setContentTitle(Title);

        mBuilder.setContentText(content);
        mBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/alarm_gentle"));
        Log.e("jmbjgjg", "onReceive: " +Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/alarm_gentle").toString() );
        mBuilder.setColor(Color.RED);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.RED);
            mChannel.enableLights(true);
            mChannel.setShowBadge(true);
            mChannel.setDescription(description);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/alarm_gentle"), audioAttributes);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel( mChannel );
            }
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(1, mBuilder.build());
    }
}
