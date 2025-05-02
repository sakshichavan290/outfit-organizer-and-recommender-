package com.example.outfitorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import com.prolificinteractive.materialcalendarview.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SelectOutfitActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private Button buttonScheduleOutfit;
    private TextView textViewSelectedOutfit;
    private ImageView imageViewShirt;

    private FirebaseAuth auth;
    private DatabaseReference scheduledRef;

    private Outfit selectedOutfit;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_outfit);

        calendarView = findViewById(R.id.calendarView);
        buttonScheduleOutfit = findViewById(R.id.buttonScheduleOutfit);
        textViewSelectedOutfit = findViewById(R.id.textViewSelectedOutfit);
        imageViewShirt = findViewById(R.id.imageViewShirt);

        auth = FirebaseAuth.getInstance();
        scheduledRef = FirebaseDatabase.getInstance().getReference("ScheduledOutfits").child(auth.getUid());

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDate = date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay();
            loadScheduledOutfit(selectedDate);
        });

        buttonScheduleOutfit.setOnClickListener(v -> {
            Intent intent = new Intent(SelectOutfitActivity.this, SelectOutfitActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            startActivity(intent);
        });

        loadAllScheduledDates();
    }

    private void loadScheduledOutfit(String date) {
        scheduledRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Outfit outfit = child.getValue(Outfit.class);
                    if (outfit != null) {
                        selectedOutfit = outfit;
                        textViewSelectedOutfit.setText(outfit.getName());
                        Glide.with(SelectOutfitActivity.this).load(outfit.getShirtImage()).into(imageViewShirt);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void loadAllScheduledDates() {
        scheduledRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String[] dateParts = child.getKey().split("-");
                    if (dateParts.length == 3) {
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]) - 1;  // Month is zero-based in CalendarDay
                        int day = Integer.parseInt(dateParts[2]);
                        CalendarDay calDay = CalendarDay.from(year, month, day);

                        // Create a list of CalendarDay for the decorator
                        List<CalendarDay> calendarDays = new ArrayList<>();
                        calendarDays.add(calDay);

                        // Pass the color for the dot and the list of dates to the EventDecorator
                        int color = ContextCompat.getColor(SelectOutfitActivity.this, R.color.colorAccent);
                        calendarView.addDecorator(new DotDecorator(color, new HashSet<>(calendarDays)));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
