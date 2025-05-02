package com.example.outfitorganizer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private EditText email;
    private EditText passwordField;
    private ProgressBar progressBar;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private FrameLayout emojiContainer;
    private static final String TAG = "LoginActivity";

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "Already logged in as " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(login.this, homepage.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbutton);
        emojiContainer = findViewById(R.id.emojiContainer); // initialize emoji container
        TextView signupButton = findViewById(R.id.signupbtn);
        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView); // Add Forgot Password TextView

        // Hide emoji container initially
        emojiContainer.setVisibility(View.GONE);

        // Handle login
        loginButton.setOnClickListener(view -> {
            String username = email.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (username.isEmpty()) {
                email.setError("Email cannot be empty");
                email.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                email.setError("Enter a valid email");
                email.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordField.setError("Password cannot be empty");
                passwordField.requestFocus();
                return;
            }

            if (!isNetworkAvailable()) {
                Toast.makeText(this, "No internet connection. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if (task.getException() != null) {
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        // Handle sign-up
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Handle Forgot Password
        forgotPasswordTextView.setOnClickListener(view -> {
            showForgotPasswordDialog();
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(login.this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
            showEmojiSlideshow(); // Show emoji animation
            new Handler().postDelayed(() -> {
                startActivity(new Intent(login.this, homepage.class));
                finish();
            }, 5000); // Wait for 5 seconds
        }
    }

    private void showEmojiSlideshow() {
        emojiContainer.setVisibility(View.VISIBLE); // Make it visible now

        String[] emojis = {"👗", "👚", "🧣", "🧥", "👠", "👜"};
        TextView emojiView = new TextView(this);
        emojiView.setTextSize(54);
        emojiView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        emojiView.setAlpha(0f);
        emojiView.setScaleX(0f);
        emojiView.setScaleY(0f);

        emojiContainer.removeAllViews();
        emojiContainer.addView(emojiView);

        Handler handler = new Handler();
        final int delay = 800;
        final int[] index = {0};

        Runnable slideshow = new Runnable() {
            @Override
            public void run() {
                if (index[0] >= emojis.length) {
                    emojiContainer.removeAllViews();
                    emojiContainer.setVisibility(View.GONE);
                    return;
                }

                emojiView.setText(emojis[index[0]]);
                emojiView.setAlpha(0f);
                emojiView.setScaleX(0f);
                emojiView.setScaleY(0f);

                emojiView.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .withEndAction(() -> emojiView.animate()
                                .alpha(0f)
                                .scaleX(0f)
                                .scaleY(0f)
                                .setDuration(300)
                                .start())
                        .start();

                index[0]++;
                handler.postDelayed(this, delay);
            }
        };

        handler.post(slideshow);
    }

    // Show a dialog to let the user enter their email for password reset
    private void showForgotPasswordDialog() {
        final EditText emailInput = new EditText(this);
        emailInput.setHint("Enter your email");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setPadding(50, 20, 50, 20);

        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter your email address to receive a reset link.")
                .setView(emailInput)
                .setPositiveButton("Send Reset Link", (dialog, which) -> {
                    String emailAddress = emailInput.getText().toString().trim();
                    if (emailAddress.isEmpty()) {
                        Toast.makeText(login.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendPasswordResetEmail(emailAddress);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Send the password reset email using Firebase Authentication
    private void sendPasswordResetEmail(String emailAddress) {
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(login.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
