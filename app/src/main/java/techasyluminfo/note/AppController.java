package techasyluminfo.note;

import android.app.Application;

import techasyluminfo.note.database.NoteRoomDatabase;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NoteRoomDatabase.getDatabase(this);
    }

}
