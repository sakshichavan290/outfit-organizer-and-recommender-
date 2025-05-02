package com.example.outfitorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.prolificinteractive.materialcalendarview.*;

import java.util.*;

public class ScheduleOutfitActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private Button buttonScheduleOutfit;
    private TextView textViewSelectedOutfit;
    private ImageView imageViewShirt;

    private FirebaseAuth auth;
    private DatabaseReference scheduledRef;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_outfit);

        // Initialize UI components
        calendarView = findViewById(R.id.calendarView);
        buttonScheduleOutfit = findViewById(R.id.buttonScheduleOutfit);
        textViewSelectedOutfit = findViewById(R.id.textViewSelectedOutfit);
        imageViewShirt = findViewById(R.id.imageViewShirt);

        auth = FirebaseAuth.getInstance();
        scheduledRef = FirebaseDatabase.getInstance().getReference("ScheduledOutfits").child(auth.getUid());

        // Set up calendar view
        customizeCalendarView();

        // Handle date selection in the calendar
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDate = date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay();
            loadScheduledOutfit(selectedDate);  // Load scheduled outfit for the selected date
        });

        // Button click to schedule an outfit
        buttonScheduleOutfit.setOnClickListener(v -> {
            Intent intent = new Intent(ScheduleOutfitActivity.this, ViewOutfitsActivity.class);
            intent.putExtra("selectedDate", selectedDate); // Pass the selected date to ViewOutfitActivity
            startActivityForResult(intent, 1); // Start the activity and wait for the result
        });

        loadAllScheduledDates(); // Load all scheduled dates from Firebase
    }

    // Customize the calendar view
    private void customizeCalendarView() {
        calendarView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_NONE);
        calendarView.addDecorator(new TodayDecorator(this));  // Highlight today
        calendarView.addDecorator(new SelectedDateDecorator(calendarView));  // Highlight selected date
    }

    // Load scheduled outfit for the selected date
    private void loadScheduledOutfit(String date) {
        scheduledRef.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Outfit outfit = snapshot.getValue(Outfit.class);
                if (outfit != null) {
                    textViewSelectedOutfit.setText("Outfit scheduled");
                    Glide.with(ScheduleOutfitActivity.this).load(outfit.getShirtImage()).into(imageViewShirt);
                } else {
                    textViewSelectedOutfit.setText("No outfit scheduled");
                    imageViewShirt.setImageDrawable(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    // Load all scheduled dates and decorate them on the calendar
    private void loadAllScheduledDates() {
        scheduledRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Set<CalendarDay> decoratedDates = new HashSet<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String[] parts = child.getKey().split("-");
                    if (parts.length == 3) {
                        int year = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]) - 1;  // Correct month (0-based)
                        int day = Integer.parseInt(parts[2]);

                        decoratedDates.add(CalendarDay.from(year, month, day));
                    }
                }

                // Add red dot for scheduled dates
                calendarView.addDecorator(new DotDecorator(ContextCompat.getColor(ScheduleOutfitActivity.this, R.color.red), decoratedDates));
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    // Handle the result from ViewOutfitActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Retrieve selected outfit details from the result
            String selectedOutfitName = data.getStringExtra("selectedOutfitName");
            String selectedOutfitImage = data.getStringExtra("selectedOutfitImage");

            // Update UI
            textViewSelectedOutfit.setText("Outfit scheduled");
            Glide.with(ScheduleOutfitActivity.this).load(selectedOutfitImage).into(imageViewShirt);

            // Save to Firebase
            Outfit selectedOutfit = new Outfit(selectedOutfitName, selectedOutfitImage, "", "", "");
            scheduledRef.child(selectedDate).setValue(selectedOutfit);

            // Decorate selected date
            Set<CalendarDay> decoratedDates = new HashSet<>();
            decoratedDates.add(CalendarDay.from(Integer.parseInt(selectedDate.split("-")[0]),
                    Integer.parseInt(selectedDate.split("-")[1]) - 1,
                    Integer.parseInt(selectedDate.split("-")[2])));
            calendarView.addDecorator(new DotDecorator(ContextCompat.getColor(ScheduleOutfitActivity.this, R.color.red), decoratedDates));

            // Show confirmation toast
            Toast.makeText(ScheduleOutfitActivity.this, "Outfit scheduled for " + selectedDate, Toast.LENGTH_SHORT).show();
        }
    }
}
