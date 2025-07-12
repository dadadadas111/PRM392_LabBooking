package com.example.prm392_labbooking.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.cart.CartFragment;

public class CartExpiryReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "cart_expiry_channel";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Apply app locale to context for correct language
        com.example.prm392_labbooking.utils.LocaleUtils.applyLocale(context);

        String productName = intent.getStringExtra("product_name");
        int slotOrdinal = intent.getIntExtra("slot_ordinal", -1);
        long bookingTime = intent.getLongExtra("booking_time", 0);
        long timeLeft = intent.getLongExtra("time_left", 0);
        int notificationId = intent.getIntExtra("notification_id", 0);

        // Format expiry time as dd/MM/yyyy HH:mm
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
        String expiryTime = sdf.format(new java.util.Date(bookingTime));

        createNotificationChannel(context);
        Intent openAppIntent = new Intent(context, com.example.prm392_labbooking.presentation.MainActivity.class);
        openAppIntent.putExtra("open_cart", true);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(context.getString(R.string.cart_expiry_title))
                .setContentText(context.getString(R.string.cart_expiry_message, productName, expiryTime))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.cart_expiry_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(context.getString(R.string.cart_expiry_channel_desc));
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
