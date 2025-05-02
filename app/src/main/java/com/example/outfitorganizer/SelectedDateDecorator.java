package com.example.outfitorganizer;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.prolificinteractive.materialcalendarview.*;

public class SelectedDateDecorator implements DayViewDecorator {

    private final MaterialCalendarView calendarView;

    public SelectedDateDecorator(MaterialCalendarView calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return calendarView.getSelectedDate() != null && calendarView.getSelectedDate().equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        Drawable drawable = ContextCompat.getDrawable(calendarView.getContext(), R.drawable.selected_date_background);
        view.setBackgroundDrawable(drawable);
    }
}
