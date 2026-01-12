package com.example.ratedialogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "prefs";
    private static final String KEY_LAUNCH_COUNT = "launch_count";
    private static final String KEY_RATE_SHOWN = "rate_shown";
    private static final int SHOW_ON_LAUNCH = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton btnShowNow = findViewById(R.id.btnShowNow);
        MaterialButton btnReset = findViewById(R.id.btnReset);

        btnShowNow.setOnClickListener(v -> showRateDialog());
        btnReset.setOnClickListener(v -> {
            resetCounter();
            Toast.makeText(this, "Counter reset. Dialog will appear on 5th launch.", Toast.LENGTH_SHORT).show();
        });

        maybeShowRateDialog();
    }

    private void maybeShowRateDialog() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);

        int count = sp.getInt(KEY_LAUNCH_COUNT, 0) + 1;
        sp.edit().putInt(KEY_LAUNCH_COUNT, count).apply();

        if (!sp.getBoolean(KEY_RATE_SHOWN, false) && count == SHOW_ON_LAUNCH) {
            sp.edit().putBoolean(KEY_RATE_SHOWN, true).apply();
            showRateDialog();
        }
    }

    private void resetCounter() {
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putInt(KEY_LAUNCH_COUNT, 0)
                .putBoolean(KEY_RATE_SHOWN, false)
                .apply();
    }

    private void showRateDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_rate, null);
        RatingBar ratingBar = v.findViewById(R.id.ratingBar);
        TextView txtHint = v.findViewById(R.id.txtHint);

        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(v)
                .setCancelable(false)
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .setPositiveButton("OK", null)
                .create();

        dialog.setOnShowListener(ignored -> dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(btn -> {
                    int rating = (int) ratingBar.getRating();

                    if (rating == 0) {
                        txtHint.setText("Please select a rating");
                        return;
                    }

                    dialog.dismiss();

                    if (rating <= 3) showLowRatingDialog();
                    else showHighRatingDialog();
                }));

        ratingBar.setOnRatingBarChangeListener((rb, rating, fromUser) -> {
            int r = (int) rating;
            if (r == 0) txtHint.setText("Please select a rating");
            else if (r <= 3) txtHint.setText("Sorry to hear that. Help us improve.");
            else txtHint.setText("Thank you! We appreciate it.");
        });

        dialog.show();
    }

    private void showLowRatingDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Thank you")
                .setMessage("Would you like to leave feedback?")
                .setNegativeButton("No", (d, w) -> d.dismiss())
                .setPositiveButton("Leave feedback", (d, w) -> {
                    d.dismiss();
                    showFeedbackDialog();
                })
                .show();
    }

    private void showFeedbackDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_feedback, null);
        EditText edt = v.findViewById(R.id.edtFeedback);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Feedback")
                .setView(v)
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .setPositiveButton("Send", (d, w) -> {
                    String text = edt.getText() == null ? "" : edt.getText().toString().trim();
                    if (TextUtils.isEmpty(text)) text = "(empty feedback)";
                    Toast.makeText(this, "Thanks! Feedback: " + text, Toast.LENGTH_LONG).show();
                })
                .show();
    }

    private void showHighRatingDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Thanks for the rating")
                .setMessage("Would you like to rate on Google Play?")
                .setNegativeButton("No", (d, w) -> d.dismiss())
                .setPositiveButton("Open Play", (d, w) -> openPlayMarket())
                .show();
    }

    private void openPlayMarket() {
        String packageName = "org.telegram.messenger";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }
}
