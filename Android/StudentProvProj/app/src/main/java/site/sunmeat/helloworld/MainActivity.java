package site.sunmeat.helloworld;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn = findViewById(R.id.btnOpen);

        btn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("site.sunmeat.secondapp.OPEN");
            intent.putExtra("message", "Hello from App 1!");
            startActivity(intent);
        });
        AppDatabase db = AppDatabase.getInstance(this);
        db.studentDao().insert(new Student("Іван", "Петренко"));
        db.studentDao().insert(new Student("Олена", "Коваль"));
        db.studentDao().insert(new Student("Максим", "Іванов"));


    }
}