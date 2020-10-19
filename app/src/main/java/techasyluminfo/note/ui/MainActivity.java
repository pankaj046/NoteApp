package techasyluminfo.note.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import techasyluminfo.note.R;
import techasyluminfo.note.adapter.NoteAdapter;
import techasyluminfo.note.databinding.ActivityMainBinding;
import techasyluminfo.note.model.NoteModel;
import techasyluminfo.note.util.Constants;
import techasyluminfo.note.util.PreferenceManager;
import techasyluminfo.note.viewmodels.NoteViewModels;


public class MainActivity extends AppCompatActivity {

    private NoteViewModels noteViewModels;

   private NoteAdapter adapter;

    private ActivityMainBinding binding;

    private final int MenuItem_themeId = 0, MenuItem_viewId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(Color.WHITE);


        binding.addNoteEfb.setOnClickListener(v->{
            addNote();
        });
        noteViewModels = new ViewModelProvider(this).get(NoteViewModels.class);
        if (PreferenceManager.getBoolean(MainActivity.this, Constants.layoutOrientation)) {
            binding.noteListRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            setAdapter();
            getData();
        } else {
            binding.noteListRv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            setAdapter();
            getData();
        }


      //  getData();
    }

    private void getData(){
        noteViewModels.getAllWords().observe(MainActivity.this, new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                adapter.setNote(noteModels);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem theme = menu.add(0, MenuItem_themeId, 0, R.string.app_name);
        theme.setIcon(R.drawable.ic_theme_24dp);
        theme.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem layout_orientation = menu.add(0, MenuItem_viewId, 1, R.string.app_name);
        if (PreferenceManager.getBoolean(MainActivity.this, Constants.layoutOrientation)) {
            layout_orientation.setIcon(R.drawable.ic_gride_view_24dp);
        } else {
            layout_orientation.setIcon(R.drawable.ic_list_view_24dp);
        }

        layout_orientation.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        layout_orientation.setOnMenuItemClickListener(menuItem -> {
            if (PreferenceManager.getBoolean(MainActivity.this, Constants.layoutOrientation)) {
                PreferenceManager.saveBoolean(MainActivity.this, Constants.layoutOrientation, false);
                layout_orientation.setIcon(R.drawable.ic_list_view_24dp);
                binding.noteListRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                getData();
            } else {
                PreferenceManager.saveBoolean(MainActivity.this, Constants.layoutOrientation, true);
                layout_orientation.setIcon(R.drawable.ic_gride_view_24dp);
                binding.noteListRv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                getData();
            }
            return false;
        });

        theme.setOnMenuItemClickListener(menuItem -> {
            Toast.makeText(this, "theme", Toast.LENGTH_SHORT).show();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void addNote() {
        AddNoteFragment placeSearchFragment = new AddNoteFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        placeSearchFragment.show(fm, "AddNoteFragment");
    }

    private void setAdapter(){
        adapter = new NoteAdapter(MainActivity.this);
        binding.noteListRv.setAdapter(adapter);
    }
}