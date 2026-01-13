package site.sunmeat.helloworld;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StudentDao_Impl implements StudentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Student> __insertionAdapterOfStudent;

  public StudentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudent = new EntityInsertionAdapter<Student>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `students` (`_id`,`firstName`,`lastName`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Student entity) {
        statement.bindLong(1, entity._id);
        if (entity.firstName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.firstName);
        }
        if (entity.lastName == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.lastName);
        }
      }
    };
  }

  @Override
  public void insert(final Student student) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStudent.insert(student);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Cursor getAllStudentsBlocking() {
    final String _sql = "SELECT * FROM students ORDER BY lastName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _tmpResult = __db.query(_statement);
    return _tmpResult;
  }

  @Override
  public Cursor getStudentByIdBlocking(final long id) {
    final String _sql = "SELECT * FROM students WHERE _id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _tmpResult = __db.query(_statement);
    return _tmpResult;
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
