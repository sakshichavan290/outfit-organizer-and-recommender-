package com.example.outfitorganizer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.*;

public class TodayDecorator implements DayViewDecorator {

    private final CalendarDay today = CalendarDay.today();
    private final Context context;

    // Constructor accepting context
    public TodayDecorator(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(today);  // Only decorate today's date
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Use the provided context to get the drawable
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.today_background);
        view.setBackgroundDrawable(drawable);  // Set background drawable for today's date
    }
}
