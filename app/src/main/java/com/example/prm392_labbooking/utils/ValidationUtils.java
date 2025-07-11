package com.example.prm392_labbooking.utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.Slot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidCardholderName(String name) {
        return name != null && name.matches("^[a-zA-Z ]+$") && !name.trim().isEmpty();
    }

    public static boolean isValidCardNumber(String number) {
        return number != null && number.matches("^\\d{16}$");
    }

    public static boolean isValidExpiryDate(String expiry) {
        if (expiry == null || !expiry.matches("^\\d{2}/\\d{2}$")) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        sdf.setLenient(false);
        try {
            Date expiryDate = sdf.parse(expiry);
            Date currentDate = new Date();
            return expiryDate.after(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("^\\d{3}$");
    }

    public static boolean isValidBookingTime(Date date, List<Slot> slots) {
        long remaining = getRemainingTimeUntilBooking(date, slots);
        return remaining > 0;
    }

    public static String getMergedSlotDisplay(List<Slot> slots) {
        if (slots == null || slots.isEmpty()) return "";
        List<Slot> sorted = new java.util.ArrayList<>(slots);
        sorted.sort((a, b) -> a.ordinal() - b.ordinal());
        StringBuilder sb = new StringBuilder();
        int start = 0;
        while (start < sorted.size()) {
            int end = start;
            while (end + 1 < sorted.size() && sorted.get(end + 1).ordinal() == sorted.get(end).ordinal() + 1) {
                end++;
            }
            if (start == end) {
                sb.append(sorted.get(start).getFormattedTime());
            } else {
                sb.append(sorted.get(start).getFormattedTime().split("-")[0].trim())
                  .append(" - ")
                  .append(sorted.get(end).getFormattedTime().split("-")[1].trim());
            }
            if (end + 1 < sorted.size()) sb.append(", ");
            start = end + 1;
        }
        return sb.toString();
    }

    public static List<String> getMergedSlotDisplayList(List<Slot> slots) {
        List<String> result = new java.util.ArrayList<>();
        if (slots == null || slots.isEmpty()) return result;
        List<Slot> sorted = new java.util.ArrayList<>(slots);
        sorted.sort((a, b) -> a.ordinal() - b.ordinal());
        int start = 0;
        while (start < sorted.size()) {
            int end = start;
            while (end + 1 < sorted.size() && sorted.get(end + 1).ordinal() == sorted.get(end).ordinal() + 1) {
                end++;
            }
            if (start == end) {
                result.add(sorted.get(start).getFormattedTime());
            } else {
                result.add(sorted.get(start).getFormattedTime().split("-")[0].trim()
                        + " - " +
                        sorted.get(end).getFormattedTime().split("-")[1].trim());
            }
            start = end + 1;
        }
        return result;
    }

    public static long getRemainingTimeUntilBooking(Date date, List<Slot> slots) {
        if (date == null || slots == null || slots.isEmpty()) return -1;
        // Assume Slot has a method getStartHour() returning the start hour (int)
        Slot firstSlot = slots.stream().min((a, b) -> a.ordinal() - b.ordinal()).orElse(null);
        if (firstSlot == null) return -1;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int hour = firstSlot.getStart().getHour();
        cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        long bookingTimeMillis = cal.getTimeInMillis();
        long nowMillis = System.currentTimeMillis();
        return bookingTimeMillis - nowMillis;
    }

    public static String getLabelRelativeRemainingTime(android.content.Context context, long millis) {
        if (millis <= 0) return "0 " + context.getString(R.string.min);
        long minutes = millis / (60 * 1000);
        long hours = minutes / 60;
        long days = hours / 24;
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" ").append(context.getString(R.string.day));
        } else if (hours > 0) {
            sb.append(hours).append(" ").append(context.getString(R.string.hour));
        } else {
            sb.append(minutes).append(" ").append(context.getString(R.string.min));
        }
        return sb.toString();
    }
}
