package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import es.dmoral.toasty.Toasty; // <-- этот импорт должен работать после синхронизации

public class MainActivity extends AppCompatActivity {

    private int sectionIndex = 0;
    private String[] songSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songSections = getResources().getStringArray(R.array.song_sections);
    }

    public void f(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;
            button.setText(R.string.next_section_button);

            if (songSections.length == 0) {
                return;
            }

            String section = songSections[sectionIndex];
            sectionIndex = (sectionIndex + 1) % songSections.length;

            int toastBackground = ContextCompat.getColor(this, R.color.toast_background);
            int toastText = ContextCompat.getColor(this, R.color.toast_text);

            Toasty.custom(
                    this,
                    section,
                    null,
                    toastBackground,
                    toastText,
                    Toasty.LENGTH_LONG,
                    false,
                    true
            ).show();
        }
    }
}
