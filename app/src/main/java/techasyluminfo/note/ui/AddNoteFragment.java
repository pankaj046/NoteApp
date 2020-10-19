package techasyluminfo.note.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import techasyluminfo.note.R;

import techasyluminfo.note.dao.NoteDao;
import techasyluminfo.note.databinding.FragmentAddNoteBinding;
import techasyluminfo.note.model.NoteModel;

import static techasyluminfo.note.database.NoteRoomDatabase.INSTANCE;
import static techasyluminfo.note.database.NoteRoomDatabase.databaseWriteExecutor;


public class AddNoteFragment extends DialogFragment implements View.OnClickListener {

    FragmentAddNoteBinding binding;

    private String am_pm = "";
    private int dbHour;
    private int dbMinute;
    private int dbDate;
    private int dbMonth;
    private int dbYear;

    private boolean isReminderSet = false;

    public AddNoteFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.closeIv.setOnClickListener(v -> dismiss());
        binding.saveIv.setOnClickListener(this);
        binding.dateEt.setOnClickListener(this);
        binding.timeEt.setOnClickListener(this);
        binding.dateEt.setKeyListener(null);
        binding.timeEt.setKeyListener(null);
        binding.dateEt.setInputType(InputType.TYPE_NULL);
        binding.timeEt.setInputType(InputType.TYPE_NULL);
        binding.reminderController.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isReminderSet = true;
                    binding.reminderLayout.setVisibility(View.VISIBLE);
                } else {
                    isReminderSet = false;
                    binding.reminderLayout.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.save_iv:
                String title = "" + binding.titleTv.getText();
                String note = "" + binding.noteTv.getText();

                if (title.isEmpty()) {
                    Toast.makeText(requireContext(), "Please Enter Title", Toast.LENGTH_SHORT).show();
                } else if (note.isEmpty()) {
                    Toast.makeText(requireContext(), "Please Enter Note", Toast.LENGTH_SHORT).show();
                }else {
                    if (isReminderSet) {

                        if (TextUtils.isEmpty(binding.dateEt.getText())){
                            Toast.makeText(requireContext(), "Please Enter Reminder Date", Toast.LENGTH_SHORT).show();
                        }else if (TextUtils.isEmpty(binding.timeEt.getText())){
                            Toast.makeText(requireContext(), "Please Enter Reminder Time", Toast.LENGTH_SHORT).show();
                        }else {
                            addNoteInDB();
                        }
                    } else {
                        addNoteInDB();
                    }

                }
                break;

            case R.id.date_et:

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                cal.before(1);
                DatePickerDialog dialog = new DatePickerDialog(
                        requireContext(), (datePicker, mYear,
                                           monthOfYear, dayOfMonth) -> {
                            dbDate = dayOfMonth;
                            dbMonth = monthOfYear;
                            dbYear = mYear;
                    binding.dateEt.setText(dayOfMonth + " " + getMonth("" + monthOfYear) + " " + mYear);
                }, year, month, day);
                dialog.show();
                break;

            case R.id.time_et:
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                    if (hourOfDay < 12) {
                        am_pm = "AM";
                    } else {
                        am_pm = "PM";
                    }
                    dbHour = hourOfDay;
                    dbMinute = minute;
                    binding.timeEt.setText(hourOfDay + ":" + minute + " " + am_pm);
                }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
        }
    }

    private void addNoteInDB() {

        databaseWriteExecutor.execute(() -> {
            NoteDao dao = INSTANCE.noteDao();
            NoteModel noteModel = new NoteModel(""+binding.titleTv.getText(),
                    ""+binding.noteTv.getText(),
                    dbDate,
                    dbMonth,
                    dbYear,
                    dbHour,
                    dbMinute,
                    ""+getDate(new Date().toString()),
                    isReminderSet,
                    am_pm);
            dao.insert(noteModel);
        });
        dismiss();
    }

    private String getDate(String date) {
//        Tue Jul 12 18:35:37 IST 2016
//        String date = "Wed Sep 16 00:00:00 GMT+05:30 2020";
        String[] parts = date.split(" ");
        return parts[2]+ " " +parts[1] +" "+parts[parts.length-1];
    }


    public static String getMonth(String date) {

        String month = "";
        Log.e("sddsds", "getMonth: " + date);
        if (date.equalsIgnoreCase("00") || date.equalsIgnoreCase("0")) {
            month = "Jan";
        } else if (date.equalsIgnoreCase("01") || date.equalsIgnoreCase("1")) {
            month = "Feb";
        } else if (date.equalsIgnoreCase("02") || date.equalsIgnoreCase("2")) {
            month = "Mar";
        } else if (date.equalsIgnoreCase("03") || date.equalsIgnoreCase("3")) {
            month = "Apr";
        } else if (date.equalsIgnoreCase("04") || date.equalsIgnoreCase("4")) {
            month = "May";
        } else if (date.equalsIgnoreCase("05") || date.equalsIgnoreCase("5")) {
            month = "Jun";
        } else if (date.equalsIgnoreCase("06") || date.equalsIgnoreCase("6")) {
            month = "Jul";
        } else if (date.equalsIgnoreCase("07") || date.equalsIgnoreCase("7")) {
            month = "Aug";
        } else if (date.equalsIgnoreCase("08") || date.equalsIgnoreCase("8")) {
            month = "Sep";
        } else if (date.equalsIgnoreCase("09") || date.equalsIgnoreCase("9")) {
            month = "Oct";
        } else if (date.equalsIgnoreCase("10")) {
            month = "Nov";
        } else if (date.equalsIgnoreCase("11")) {
            month = "Dec";
        }
        return month;
    }
}