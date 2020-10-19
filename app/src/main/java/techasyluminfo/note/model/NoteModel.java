package techasyluminfo.note.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "note_tables")
public class NoteModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "note")
    private String note;
    @ColumnInfo(name = "day")
    private int day;
    @ColumnInfo(name = "month")
    private int month;
    @ColumnInfo(name = "year")
    private int year;
    @ColumnInfo(name = "hour")
    private int hour;
    @ColumnInfo(name = "minute")
    private int minute;
    @ColumnInfo(name = "lastEdited")
    private String lastEdited;
    @ColumnInfo(name = "isReminderSet")
    private boolean isReminderSet;
    @ColumnInfo(name = "ampm")
    private String AM_PM;

    public NoteModel(String title,
                     String note,
                     int day,
                     int month,
                     int year,
                     int hour,
                     int minute,
                     String lastEdited,
                     boolean isReminderSet,
                     String AM_PM) {
        this.title = title;
        this.note = note;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.lastEdited = lastEdited;
        this.isReminderSet = isReminderSet;
        this.AM_PM = AM_PM;
    }

    public String getAM_PM() {
        return AM_PM;
    }

    public void setAM_PM(String AM_PM) {
        this.AM_PM = AM_PM;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
    }

    public boolean isReminderSet() {
        return isReminderSet;
    }

    public void setReminderSet(boolean reminderSet) {
        isReminderSet = reminderSet;
    }
}
