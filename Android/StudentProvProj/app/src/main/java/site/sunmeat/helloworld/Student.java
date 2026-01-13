package site.sunmeat.helloworld;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {

    @PrimaryKey(autoGenerate = true)
    public long _id;

    public String firstName;
    public String lastName;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
