package site.sunmeat.helloworld;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract StudentDao studentDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "students.db"
            ).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
