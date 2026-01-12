package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import es.dmoral.toasty.Toasty; // <-- этот импорт должен работать после синхронизации

public class MainActivity extends AppCompatActivity {

    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void f(View view) {
        if (view instanceof Button) {
            Button b = (Button) view;

            clickCount++;
            b.setText(String.valueOf(clickCount));

            // Генерация случайного цвета для кнопки
            int red = (int) (Math.random() * 256);
            int green = (int) (Math.random() * 256);
            int blue = (int) (Math.random() * 256);
            int randomColor = Color.rgb(red, green, blue);
            b.setBackground(new ColorDrawable(randomColor));

            // Кастомный тост через Toasty
            Toasty.custom(
                    this,
                    "Clicked " + clickCount + " times!",
                    null,          // Drawable иконка (null = без иконки)
                    randomColor,   // цвет фона
                    Color.WHITE,   // цвет текста
                    Toasty.LENGTH_SHORT,
                    false,         // show icon
                    true           // with border
            ).show();
        }
    }
}
