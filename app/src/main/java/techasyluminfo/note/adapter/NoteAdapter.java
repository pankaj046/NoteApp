package techasyluminfo.note.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import techasyluminfo.note.databinding.NoteLayoutBinding;
import techasyluminfo.note.model.NoteModel;

import static techasyluminfo.note.ui.AddNoteFragment.getMonth;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<NoteModel> list;
    private Context context;

    public NoteAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(NoteLayoutBinding.inflate(inflater,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        String upperString = list.get(position).getTitle().substring(0, 1).toUpperCase()
                + list.get(position).getTitle().substring(1).toLowerCase();
        holder.noteLayoutBinding.titleTv.setText(upperString);
        holder.noteLayoutBinding.lastUpdated.setText(list.get(position).getLastEdited());
        if (list.get(position).isReminderSet()){
            holder.noteLayoutBinding.layoutReminder.setVisibility(View.VISIBLE);
            holder.noteLayoutBinding.reminder.setText(list.get(position).getDay()+" "
                    +getMonth(list.get(position).getMonth()+"")
                    + " "+list.get(position).getYear()
                    +" "+list.get(position).getHour()+":"+list.get(position).getMinute()+""+list.get(position).getAM_PM());
        }else {
            holder.noteLayoutBinding.layoutReminder.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        } else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private NoteLayoutBinding noteLayoutBinding;

        public ViewHolder(@NonNull NoteLayoutBinding noteLayoutBinding) {
            super(noteLayoutBinding.getRoot());
            this.noteLayoutBinding = noteLayoutBinding;
        }
    }

    public void setNote(List<NoteModel> noteModels) {
        list = noteModels;
        notifyDataSetChanged();
    }
}
