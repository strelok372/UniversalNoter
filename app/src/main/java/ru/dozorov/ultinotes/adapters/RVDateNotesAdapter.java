package ru.dozorov.ultinotes.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import ru.dozorov.ultinotes.KeyboardInterface;
import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.fragments.DateNotesFragment;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class RVDateNotesAdapter extends RecyclerView.Adapter<RVDateNotesAdapter.DateNoteViewHolder> {
    private final LayoutInflater inflater;
    private List<DateNoteEntity> entityList;
    private NoteViewModel viewModel;
    private static int mExpandedPosition = -1;
    private static int previousExpandedPosition = -1;
    private KeyboardInterface keyboardInterface;
    private DateTimePickerDialogCall dtp;

    public RVDateNotesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(NoteViewModel.class);
    }

    @NonNull
    @Override
    public DateNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.date_note_item_expanded, parent, false);
        return new DateNoteViewHolder(itemView, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull final DateNoteViewHolder holder, final int position) {
        if (entityList != null) {
            if (position == getItemCount() - 1) {
                holder.itemView.setVisibility(View.INVISIBLE);
                holder.itemView.setEnabled(false);
                return;
            } else {
                holder.itemView.setEnabled(true);
                holder.itemView.setVisibility(View.VISIBLE);
            }
            DateNoteEntity d = entityList.get(position);
//            if (position == previousExpandedPosition){
//                d.setDescription(holder.text.getText().toString());
//                viewModel.update(d);
//                keyboardInterface.close();
//                previousExpandedPosition = -1;
//            }

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


            holder.myCustomEditTextListener.updatePosition(position);

            holder.text.setText(d.getDescription());

            final boolean isExpanded = position == mExpandedPosition;

            if (isExpanded) {
                holder.text.setMaxLines(Integer.MAX_VALUE);
//                holder.text.setSingleLine(false);
//                holder.text.setClickable(true);
//                holder.text.setFocusable(true);
                holder.text.setEnabled(true);
//                holder.text.focus();
                holder.expandButton.setImageResource(R.drawable.ic_arrow_up);
                holder.dtHolder.setVisibility(View.GONE);
                if (holder.text.getText().length() == 0)
                    keyboardInterface.showKeyboard(holder.text);
            } else {
                if (holder.text.getText().length() == 0) {
                    keyboardInterface.hideKeyboard();
                    viewModel.delete(entityList.get(position));
                }
//                holder.text.setMaxLines(1);
//                holder.text.setSingleLine();
                holder.text.setEnabled(false);
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
//                    if (isExpanded) {
//                        DateNoteEntity s = entityList.get(position);
//                        s.setDescription(holder.text.getText().toString());
//                        viewModel.update(s);
//                    }

                    if (isExpanded) {
                        keyboardInterface.hideKeyboard();
                        DateNoteEntity temp = entityList.get(position);
                        temp.setDescription(holder.text.getText().toString());
                        viewModel.update(temp);
                    }

//                    else keyboardInterface.showKeyboard(holder.text);

                    mExpandedPosition = isExpanded ? -1 : position;
                    notifyItemChanged(previousExpandedPosition); //??
                    notifyItemChanged(position); //??
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
//        for (DateNoteEntity s : entityList){
//            Log.i("EWLRWELKRLKWE", "UPDATING!1");
//            viewModel.update(s);
//        }
    }

    public List<DateNoteEntity> getEntityList() {
        return entityList;
    }

    public void setKeyboardInterface(KeyboardInterface keyboardInterface) {
        this.keyboardInterface = keyboardInterface;
    }

    public void setDateTimePicker(DateTimePickerDialogCall dtp){
        this.dtp = dtp;
    }

    @Override
    public int getItemCount() {
        if (entityList != null && entityList.size() > 0)
            return entityList.size() + 1;
        else {
            return 0;
        }
    }



    public void expandLastAdded() {
        DateNoteEntity temp = entityList.get(0);
        for (DateNoteEntity d : entityList) {
            if (d.getId() > temp.getId()) temp = d;
        }
//        keyboardInterface.showKeyboard(null);
        mExpandedPosition = entityList.indexOf(temp);
    }

    public void setNotes(List<DateNoteEntity> list) {

        if (entityList != null && entityList.size() < list.size()) {
            entityList = list;
            expandLastAdded();
        } else
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
        MyCustomEditTextListener myCustomEditTextListener;

        public DateNoteViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener) {

            super(itemView);
            date = itemView.findViewById(R.id.tv_date_dn_item);
            text = itemView.findViewById(R.id.et_text_dn_item);
            deleteButton = itemView.findViewById(R.id.iv_delete_button);
            details = itemView.findViewById(R.id.i_date_note_item);
            expandButton = itemView.findViewById(R.id.iv_date_note_expand);
            datePicker = itemView.findViewById(R.id.iv_date_pick);
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dtp.setDate(entityList.get(getAdapterPosition()));
                }
            });
            timePicker = itemView.findViewById(R.id.iv_time_pick);
            timePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dtp.setTime(entityList.get(getAdapterPosition()));
                }
            });
            time = itemView.findViewById(R.id.tv_time_dn_item);
            dtHolder = itemView.findViewById(R.id.ll_date_time_holder);
            pickedDate = itemView.findViewById(R.id.tv_picked_date);
            pickedTime = itemView.findViewById(R.id.tv_picked_time);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus)
//                        keyboardInterface.hideKeyboard();
                }
            });

            this.myCustomEditTextListener = myCustomEditTextListener;

            text.addTextChangedListener(myCustomEditTextListener);

            text.setHorizontallyScrolling(false);
            text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        keyboardInterface.hideKeyboard();
                        mExpandedPosition = -1;
                        DateNoteEntity temp = entityList.get(getAdapterPosition());
                        temp.setDescription(text.getText().toString());
                        viewModel.update(temp);
                        return true;
                    } else return false;
                }
            });

//            text.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                        switch (keyCode) {
//                            case KeyEvent.KEYCODE_ENTER:
//                            case KeyEvent.KEYCODE_DPAD_CENTER:
//                                keyboardInterface.hideKeyboard();
//                                return true;
//                            default:
//                                break;
//                        }
//                    }
//                    return false;
//                }
//            });
//            text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus){
//                        keyboardInterface.showKeyboard(v);
//                    }else {
//                        if (entityList != null){
//                            for (DateNoteEntity s : entityList){
//                                viewModel.update(s);
//                            }
//                            Log.i("EWLRWELKRLKWE", "UPDATING!1");
//                        }
//                    }
//                }
//            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keyboardInterface.hideKeyboard();
                    viewModel.delete(entityList.get(getAdapterPosition()));
                    previousExpandedPosition = -1;
                    mExpandedPosition = -1;
                }
            });
        }
    }


    private class MyCustomEditTextListener implements TextWatcher {
        private Integer position;
        DateNoteEntity s;

        public void updatePosition(int position) {
            this.position = position;
            s = entityList.get(position);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            s.setDescription(editable.toString());
        }

    }
    public interface DateTimePickerDialogCall{
        public void setTime(DateNoteEntity entity);
        public void setDate(DateNoteEntity entity);
    }
}
