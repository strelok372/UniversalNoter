package ru.dozorov.notesanddates.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.room.entities.DateNoteEntity;

public class RVDateNotesAdapter extends RecyclerView.Adapter<RVDateNotesAdapter.DateNoteViewHolder> {
    private final LayoutInflater inflater;
    private List<DateNoteEntity> entityList;

    public RVDateNotesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public DateNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.date_note_item, parent, false);
        return new DateNoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DateNoteViewHolder holder, int position) {
        if (entityList != null) {
            DateNoteEntity d = entityList.get(position);
            if (d.getTime() == null)
                holder.date.setText(d.getDate().format(DateTimeFormatter.ofPattern("dd.MM")));
            else
                holder.date.setText(d.getDate().format(DateTimeFormatter.ofPattern("dd.MM"))
                + " " + d.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            holder.text.setText(d.getDescription());
        } else
            holder.text.setText("There are no records yet");
    }

    @Override
    public int getItemCount() {
        if (entityList != null)
            return entityList.size();
        else
            return 0;
    }

    public void setNotes(List<DateNoteEntity> list){
        entityList = list;
        notifyDataSetChanged();
    }

    class DateNoteViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        TextView date;
        ImageView deleteButton;


        public DateNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_date_dn_item);
            text = itemView.findViewById(R.id.tv_text_dn_item);
            deleteButton = itemView.findViewById(R.id.iv_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    get
                    entityList.remove(getAdapterPosition());
                    //удаление из лайвдаты
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //открыть окно с текущей записью
                }
            });

        }
    }
}
