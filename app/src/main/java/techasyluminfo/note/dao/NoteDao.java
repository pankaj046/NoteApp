package techasyluminfo.note.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import techasyluminfo.note.model.NoteModel;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NoteModel noteModel);

    @Query("DELETE FROM note_tables")
    void deleteAll();

    @Query ("DELETE FROM note_tables WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * from note_tables ORDER BY id DESC")
    LiveData<List<NoteModel>> getAllNotes();

    @Query("SELECT * from note_tables WHERE id = :id")
    NoteModel getNoteById(long id);

    @Query("UPDATE note_tables SET title=:mTtitle, note=:mNote, day=:day, month=:month, year=:year, hour=:hour, minute=:minute, lastEdited=:mLastEdited, isReminderSet=:mIsReminderSet, ampm=:mAM_PM WHERE id = :id")
    void update(
                String mTtitle,
                String mNote,
                int day,
                int month,
                int year,
                int hour,
                int minute,
                String mLastEdited,
                boolean mIsReminderSet,
                String mAM_PM,
                long id);
}
