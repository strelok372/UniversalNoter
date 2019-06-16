package ru.dozorov.notesanddates.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.room.entities.SimpleNoteEntity;
import ru.dozorov.notesanddates.viewmodel.NoteViewModel;

public class RVSimpleNotesAdapter extends RecyclerView.Adapter<RVSimpleNotesAdapter.SimpleNoteViewHolder> {
    List<SimpleNoteEntity> list;
    private final LayoutInflater inflater;

    public RVSimpleNotesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SimpleNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.simple_note_item, parent, false);
        return new SimpleNoteViewHolder(v);
    }

    public void setNotes(List<SimpleNoteEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleNoteViewHolder holder, int position) {
        if (list != null){
            Log.i("DEBUG_LOG_MY: ", String.valueOf(list.size()));
            holder.title.setText(list.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    class SimpleNoteViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public SimpleNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_simple_note_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //открыть окошко заметки
                }
            });
        }
    }
}
