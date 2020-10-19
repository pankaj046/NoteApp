package techasyluminfo.note.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import techasyluminfo.note.dao.NoteDao;
import techasyluminfo.note.database.NoteRoomDatabase;
import techasyluminfo.note.model.NoteModel;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<NoteModel>> mAllWords;

    public NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllWords = mNoteDao.getAllNotes();
    }

    public LiveData<List<NoteModel>> getAllNotes() {
        return mAllWords;
    }

    public void insert(NoteModel noteModel) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.insert(noteModel);
        });
    }
}
