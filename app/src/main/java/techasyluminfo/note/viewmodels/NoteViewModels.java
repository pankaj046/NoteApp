package techasyluminfo.note.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import techasyluminfo.note.model.NoteModel;
import techasyluminfo.note.repository.NoteRepository;

public class NoteViewModels extends AndroidViewModel {

    private NoteRepository mRepository;
    private LiveData<List<NoteModel>> allNotes;

    public NoteViewModels(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        allNotes = mRepository.getAllNotes();
    }

    public LiveData<List<NoteModel>> getAllWords() {
        return allNotes;
    }

    public void insert(NoteModel noteModel) {
        mRepository.insert(noteModel);
    }
}