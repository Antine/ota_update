package sk.fourq.otaupdate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * @Author lixiaobin
 * @Date 2022/12/14
 * @Description
 */
public class DownloadNotificationHelper {
    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private final Context context;
    private static final int NOTIFICATION_ID3 = 1003;

    public DownloadNotificationHelper(Context context) {
        this.context = context;
    }

    public void sendProgressNotification() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String channelId = applicationInfo.packageName;
        builder = new Notification.Builder(context);
        builder.setSmallIcon(applicationInfo.icon)
                .setContentTitle("Software Updates")
                .setContentText("0%")
                .setProgress(100, 10, true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "download_channel", NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            channel.setImportance(NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        builder.setOnlyAlertOnce(true);
        Notification n = builder.build();
        notificationManager.notify(NOTIFICATION_ID3, n);
    }

    public void progress(int progress) {
        if (builder != null) {
            Log.d("lxb", "progress: " + progress);
            builder.setProgress(100, progress, false);
            builder.setContentText(progress + "%");
            Notification n = builder.build();
            notificationManager.notify(NOTIFICATION_ID3, n);
        }
    }

    public void downloadNotificationEnd() {
        if (notificationManager != null) {
            //更新通知内容
            notificationManager.cancel(NOTIFICATION_ID3);
            builder.setProgress(0, 0, false);
            builder.setContentText("The package is downloaded");
            Notification n = builder.build();
            notificationManager.notify(NOTIFICATION_ID3, n);
        }
    }

    public void clearNotification() {
        //单利的系统服务
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID3);
        }
    }
}
