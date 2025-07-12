package com.example.prm392_labbooking.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.domain.model.Slot;
import java.util.Calendar;
import java.util.List;

public class CartExpiryScheduler {
    // Time offsets in ms
    public static final long[] OFFSETS = {
            24 * 60 * 60 * 1000L, // 24 hours
            10 * 60 * 60 * 1000L, // 10 hours
            1 * 60 * 60 * 1000L   // 1 hour
    };

    public static void scheduleExpiryAlarms(Context context, CartItem item, int cartItemId) {
        List<Slot> slots = item.getSlots();
        String productName = item.getProduct().getName();
        for (Slot slot : slots) {
            long bookingTime = getSlotStartMillis(item.getDate(), slot);
            for (long offset : OFFSETS) {
                long triggerAt = bookingTime - offset;
                if (triggerAt > System.currentTimeMillis()) {
                    int notificationId = getNotificationId(cartItemId, slot.ordinal(), offset);
                    Intent intent = new Intent(context, CartExpiryReceiver.class);
                    intent.putExtra("product_name", productName);
                    intent.putExtra("slot_ordinal", slot.ordinal());
                    intent.putExtra("booking_time", bookingTime);
                    intent.putExtra("time_left", offset);
                    intent.putExtra("notification_id", notificationId);
                    PendingIntent pi = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
                }
            }
        }
    }

    public static void cancelExpiryAlarms(Context context, CartItem item, int cartItemId) {
        List<Slot> slots = item.getSlots();
        for (Slot slot : slots) {
            for (long offset : OFFSETS) {
                int notificationId = getNotificationId(cartItemId, slot.ordinal(), offset);
                Intent intent = new Intent(context, CartExpiryReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                am.cancel(pi);
            }
        }
    }

    public static void pushImmediateNotification(Context context, CartItem item, int cartItemId) {
        List<Slot> slots = item.getSlots();
        if (slots == null || slots.isEmpty()) return;
        String productName = item.getProduct().getName();
        Slot slot = slots.get(0); // Only notify for the first slot
        long bookingTime = getSlotStartMillis(item.getDate(), slot);
        int notificationId = getNotificationId(cartItemId, slot.ordinal(), 0); // 0 offset for immediate
        Intent intent = new Intent(context, CartExpiryReceiver.class);
        intent.putExtra("product_name", productName);
        intent.putExtra("slot_ordinal", slot.ordinal());
        intent.putExtra("booking_time", bookingTime);
        intent.putExtra("time_left", 0L);
        intent.putExtra("notification_id", notificationId);
        context.sendBroadcast(intent);
    }

    private static int getNotificationId(int cartItemId, int slotOrdinal, long offset) {
        return (cartItemId * 1000) + (slotOrdinal * 10) + (int)(offset / (60 * 60 * 1000));
    }

    public static long getSlotStartMillis(java.util.Date date, Slot slot) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, slot.getStart().getHour());
        cal.set(Calendar.MINUTE, slot.getStart().getMinute());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
