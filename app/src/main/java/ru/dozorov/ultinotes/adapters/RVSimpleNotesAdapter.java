package ru.dozorov.ultinotes.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;

public class RVSimpleNotesAdapter extends RecyclerView.Adapter<RVSimpleNotesAdapter.SimpleNoteViewHolder> {
    List<SimpleNoteEntity> list;
    OnSimpleItemClickListener listener;
    private final LayoutInflater inflater;

    public RVSimpleNotesAdapter(Context context) {
        ((FragmentActivity) context).getSupportFragmentManager();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SimpleNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.simple_note_item, parent, false);
        return new SimpleNoteViewHolder(v);
    }

    public void setNotes(List<SimpleNoteEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleNoteViewHolder holder, int position) {
        if (list != null) {
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

    class SimpleNoteViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public SimpleNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_simple_note_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                        listener.onItemClick(list.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnSimpleItemClickListener {
        void onItemClick(SimpleNoteEntity entity);
    }

    public void setOnItemClickListener(OnSimpleItemClickListener listener) {
        this.listener = listener;
    }

}
