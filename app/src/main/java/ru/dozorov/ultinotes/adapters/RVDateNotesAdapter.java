package ru.dozorov.ultinotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class RVDateNotesAdapter extends RecyclerView.Adapter<RVDateNotesAdapter.DateNoteViewHolder> {
    private final LayoutInflater inflater;
    private List<DateNoteEntity> entityList;
    private NoteViewModel viewModel;
    private static int mExpandedPosition = -1;
    private static int previousExpandedPosition = -1;

    public RVDateNotesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(NoteViewModel.class);
    }

    @NonNull
    @Override
    public DateNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.date_note_item_expanded, parent, false);
        return new DateNoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DateNoteViewHolder holder, final int position) {

        if (entityList != null) {
            DateNoteEntity d = entityList.get(position);

            if (d.getTime() == null) {
                holder.time.setVisibility(View.GONE);
                holder.date.setText(d.getDate().format(DateTimeFormatter.ofPattern("dd.MM")));
                holder.pickedDate.setText(d.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            } else {
                holder.time.setVisibility(View.VISIBLE);
                holder.date.setText(d.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                holder.pickedDate.setText(d.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                holder.time.setText(d.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                holder.pickedTime.setText(d.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }

            holder.text.setText(d.getDescription());

            final boolean isExpanded = position == mExpandedPosition;

            if (isExpanded) {
                holder.text.setSingleLine(false);
                holder.expandButton.setImageResource(R.drawable.ic_arrow_up);
                holder.dtHolder.setVisibility(View.GONE);
            } else {
                holder.text.setSingleLine();
                holder.expandButton.setImageResource(R.drawable.ic_arrow_down);
                holder.dtHolder.setVisibility(View.VISIBLE);
            }

            holder.details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.itemView.setActivated(isExpanded); //??

            if (isExpanded)
                previousExpandedPosition = position;

            holder.expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1 : position;
                    notifyItemChanged(previousExpandedPosition); //??
                    notifyItemChanged(position); //??
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (entityList != null)
            return entityList.size();
        else
            return 0;
    }

    public void setNotes(List<DateNoteEntity> list) {
        entityList = list;
        notifyDataSetChanged();
    }

    class DateNoteViewHolder extends RecyclerView.ViewHolder {
        EditText text;
        TextView date;
        ImageView deleteButton;
        View details;
        ImageView expandButton;
        ImageView datePicker;
        ImageView timePicker;
        TextView pickedDate;
        TextView pickedTime;
        TextView time;
        View dtHolder;


        public DateNoteViewHolder(@NonNull View itemView) {

            super(itemView);
            date = itemView.findViewById(R.id.tv_date_dn_item);
            text = itemView.findViewById(R.id.et_text_dn_item);
            deleteButton = itemView.findViewById(R.id.iv_delete_button);
            details = itemView.findViewById(R.id.i_date_note_item);
            expandButton = itemView.findViewById(R.id.iv_date_note_expand);
            datePicker = itemView.findViewById(R.id.iv_date_pick);
            timePicker = itemView.findViewById(R.id.iv_time_pick);
            time = itemView.findViewById(R.id.tv_time_dn_item);
            dtHolder = itemView.findViewById(R.id.ll_date_time_holder);
            pickedDate = itemView.findViewById(R.id.tv_picked_date);
            pickedTime = itemView.findViewById(R.id.tv_picked_time);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.delete(entityList.get(getAdapterPosition()));
                }
            });
        }
    }
}
