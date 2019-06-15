package ru.dozorov.notesanddates.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.room.entities.ToDoEntity;

public class RVToDoListAdapter extends RecyclerView.Adapter<RVToDoListAdapter.ToDoViewHolder> {
    private final LayoutInflater inflater;
    private List<ToDoEntity> entityList;

    public RVToDoListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.to_do_item_tv, parent);
        return new ToDoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        if (entityList != null)
            holder.text.setText(entityList.get(position).getText());

    }

    @Override
    public int getItemCount() {
        if (entityList != null)
            return entityList.size();
        else return 0;
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        CheckBox checkBox;
        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_to_do_text);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //сделать запись неактивной
                }
            });
        }
    }
}
