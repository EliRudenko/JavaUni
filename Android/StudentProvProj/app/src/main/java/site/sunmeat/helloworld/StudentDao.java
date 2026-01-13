package site.sunmeat.helloworld;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StudentDao {

    @Insert
    void insert(Student student);

    @Query("SELECT * FROM students ORDER BY lastName")
    Cursor getAllStudentsBlocking();

    @Query("SELECT * FROM students WHERE _id = :id")
    Cursor getStudentByIdBlocking(long id);
}
