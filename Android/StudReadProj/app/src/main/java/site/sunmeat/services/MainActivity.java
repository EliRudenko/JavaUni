package site.sunmeat.services;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final Uri URI =
            Uri.parse("content://site.sunmeat.helloworld.provider/students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        String[] from = {"firstName", "lastName"};
        int[] to = {android.R.id.text1, android.R.id.text2};

        Cursor cursor = getContentResolver().query(
                URI, null, null, null, null);

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(
                        this,
                        android.R.layout.simple_list_item_2,
                        cursor,
                        from,
                        to,
                        0
                );

        listView.setAdapter(adapter);
    }
}
