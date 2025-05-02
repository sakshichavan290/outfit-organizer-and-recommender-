package com.example.outfitorganizer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText newPasswordField;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI components
        newPasswordField = findViewById(R.id.newPassword);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // Handle reset password logic
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordField.getText().toString().trim();

                if (newPassword.isEmpty()) {
                    newPasswordField.setError("Password cannot be empty");
                    newPasswordField.requestFocus();
                } else {
                    // Placeholder: Save new password (implement secure storage/database)
                    Toast.makeText(ForgotPasswordActivity.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                }
            }
        });
    }
}
