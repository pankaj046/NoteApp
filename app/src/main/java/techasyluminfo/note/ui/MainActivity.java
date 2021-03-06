package techasyluminfo.note.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import techasyluminfo.note.R;
import techasyluminfo.note.adapter.NoteAdapter;
import techasyluminfo.note.dao.NoteDao;
import techasyluminfo.note.databinding.ActivityMainBinding;
import techasyluminfo.note.listeners.NoteClickListener;
import techasyluminfo.note.model.NoteModel;
import techasyluminfo.note.util.Constants;
import techasyluminfo.note.util.PreferenceManager;
import techasyluminfo.note.viewmodels.NoteViewModels;

import static techasyluminfo.note.database.NoteRoomDatabase.INSTANCE;
import static techasyluminfo.note.database.NoteRoomDatabase.databaseWriteExecutor;


public class MainActivity extends AppCompatActivity {

    private NoteViewModels noteViewModels;

    private NoteAdapter adapter;
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    public static List<NoteModel> models;
    private final int MenuItem_themeId = 0, MenuItem_viewId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        Bundle mBundle = new Bundle();
        try {
            Long id = mBundle.getLong(Constants.id);
            if (id!=null && id>0)
            databaseWriteExecutor.execute(() -> {
                NoteDao dao = INSTANCE.noteDao();
                addNote(dao.getNoteById(id));
            });

        }catch (Exception e){
            Log.e(TAG, "onCreate: "+e);
        }

        binding.noteListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.addNoteEfb.extend();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                    binding.addNoteEfb.shrink();
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    binding.addNoteEfb.shrink();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                binding.addNoteEfb.shrink();
            }
        });


        binding.addNoteEfb.setOnClickListener(v->{
            addNote();
        });
        noteViewModels = new ViewModelProvider(this).get(NoteViewModels.class);
        if (PreferenceManager.getBoolean(MainActivity.this, Constants.layoutOrientation)) {
            binding.noteListRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            runLayoutAnimation(binding.noteListRv);
            setAdapter();
            getData();
        } else {
            binding.noteListRv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            runLayoutAnimation(binding.noteListRv);
            setAdapter();
            getData();
        }


      //  getData();
    }

    private void getData(){
        noteViewModels.getAllWords().observe(MainActivity.this, new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                if (noteModels.size()>0){
                    binding.noteListRv.setVisibility(View.VISIBLE);
                    binding.emptyList.setVisibility(View.GONE);
                }else {
                    binding.emptyList.setVisibility(View.VISIBLE);
                    binding.noteListRv.setVisibility(View.GONE);
                }
                models = noteModels;
                adapter.setNote(noteModels);
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
//| ItemTouchHelper.DOWN | ItemTouchHelper.UP
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.alert_dialog_layout);
            dialog.setCancelable(false);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            MaterialButton cancel = (MaterialButton) dialog.findViewById(R.id.cancel);
            MaterialButton ok = (MaterialButton) dialog.findViewById(R.id.delete);

            cancel.setOnClickListener(v -> {
                dialog.dismiss();
                getData();
            });
            ok.setOnClickListener(v -> {
                databaseWriteExecutor.execute(() -> {
                    NoteDao dao = INSTANCE.noteDao();
                    dao.deleteById(models.get(position).getId());
                });
                getData();
                dialog.dismiss();
            });
            dialog.show();
        }
    };


    private void runLayoutAnimation(final RecyclerView recyclerView) {
        if (recyclerView.getAdapter()!=null){
            final Context context = recyclerView.getContext();
            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);
            recyclerView.setLayoutAnimation(controller);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem theme = menu.add(0, MenuItem_themeId, 0, R.string.app_name);
//        theme.setIcon(R.drawable.ic_theme_24dp);
//        theme.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        getData();
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
                binding.noteListRv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                runLayoutAnimation(binding.noteListRv);
                getData();
            } else {
                PreferenceManager.saveBoolean(MainActivity.this, Constants.layoutOrientation, true);
                layout_orientation.setIcon(R.drawable.ic_gride_view_24dp);
                binding.noteListRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                runLayoutAnimation(binding.noteListRv);
                getData();
            }
            return false;
        });

//        theme.setOnMenuItemClickListener(menuItem -> {
//            Toast.makeText(this, "theme", Toast.LENGTH_SHORT).show();
//            return false;
//        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void addNote() {
        AddNoteFragment addNoteFragment = new AddNoteFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        addNoteFragment.show(fm, "AddNoteFragment");
    }

    private void addNote(NoteModel noteModel) {
        AddNoteFragment addNoteFragment = new AddNoteFragment(noteModel);
        FragmentManager fm = this.getSupportFragmentManager();
        addNoteFragment.show(fm, "AddNoteFragment");
    }

    private void setAdapter(){
        adapter = new NoteAdapter(MainActivity.this, new NoteClickListener() {
            @Override
            public void getNoteId(long Id) {
                databaseWriteExecutor.execute(() -> {
                    NoteDao dao = INSTANCE.noteDao();
                    addNote(dao.getNoteById(Id));
                });

            }
        });
        binding.noteListRv.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.noteListRv);
    }
}