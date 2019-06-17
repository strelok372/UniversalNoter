package ru.dozorov.notesanddates.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.room.entities.ToDoEntity;
import ru.dozorov.notesanddates.viewmodel.NoteViewModel;

public class RVToDoListAdapter extends RecyclerView.Adapter<RVToDoListAdapter.ToDoViewHolder> {
    private final LayoutInflater inflater;
    private List<ToDoEntity> entityList;
    NoteViewModel viewModel;

    public RVToDoListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        viewModel = ViewModelProviders.of((FragmentActivity)context).get(NoteViewModel.class);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.to_do_item, parent, false);
        return new ToDoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        if (entityList != null)
            holder.text.setText(entityList.get(position).getText());
    }

    public void setNotes(List<ToDoEntity> list){
        entityList = list;
        notifyDataSetChanged();
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
        public ToDoViewHolder(@NonNull final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_to_do_text);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation anim = AnimationUtils.loadAnimation(inflater.getContext(), R.anim.item_animation_swap_right);
                    anim.setDuration(300);
                    itemView.startAnimation(anim);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.delete(entityList.get(getAdapterPosition()));
                        }
                    }, 300);
                }
            });
        }
    }
}
