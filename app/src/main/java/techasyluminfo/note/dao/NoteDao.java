package techasyluminfo.note.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
    LiveData<NoteModel> getNoteById(long id);
}
