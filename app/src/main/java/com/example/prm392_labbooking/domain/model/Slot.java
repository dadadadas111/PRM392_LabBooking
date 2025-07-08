package com.example.prm392_labbooking.domain.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public enum Slot {
    SLOT_08_09(LocalTime.of(8, 0), LocalTime.of(9, 0)),
    SLOT_09_10(LocalTime.of(9, 0), LocalTime.of(10, 0)),
    SLOT_10_11(LocalTime.of(10, 0), LocalTime.of(11, 0)),
    SLOT_11_12(LocalTime.of(11, 0), LocalTime.of(12, 0)),
    SLOT_12_13(LocalTime.of(12, 0), LocalTime.of(13, 0)),
    SLOT_13_14(LocalTime.of(13, 0), LocalTime.of(14, 0)),
    SLOT_14_15(LocalTime.of(14, 0), LocalTime.of(15, 0)),
    SLOT_15_16(LocalTime.of(15, 0), LocalTime.of(16, 0)),
    SLOT_16_17(LocalTime.of(16, 0), LocalTime.of(17, 0)),
    SLOT_17_18(LocalTime.of(17, 0), LocalTime.of(18, 0)),
    SLOT_18_19(LocalTime.of(18, 0), LocalTime.of(19, 0)),
    SLOT_19_20(LocalTime.of(19, 0), LocalTime.of(20, 0));

    private final LocalTime start;
    private final LocalTime end;

    Slot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public String getFormattedTime() {
        return start + " - " + end;
    }

    @Override
    public String toString() {
        return getFormattedTime();
    }

    public static void printAllSlots() {
        for (Slot slot : Slot.values()) {
            System.out.println(slot.name() + ": " + slot.getStart() + " - " + slot.getEnd());
        }
    }
}
