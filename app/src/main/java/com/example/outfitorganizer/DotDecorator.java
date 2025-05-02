package com.example.outfitorganizer;

import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class DotDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public DotDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);  // Use HashSet for efficient lookup
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);  // Apply decoration only if date is in list
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));  // Add dot below the date
    }
}
