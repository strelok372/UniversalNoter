package ru.dozorov.notesanddates.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.room.entities.DateNoteEntity;
import ru.dozorov.notesanddates.viewmodel.NoteViewModel;

public class AddDateNoteFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_DATE = 2;
    LocalDate localDate;
    LocalTime localTime;
    private EditText description;
    private ImageView datePickerButton;
    private ImageView timePickerButton;
    private Button addButton;
    private Button cancelButton;
    private TextView pickedTime;
    private TextView pickedDate;
    private NoteViewModel model;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.date_note_add_layout, container, false);
        description = rootView.findViewById(R.id.et_date_note_description);
        datePickerButton = rootView.findViewById(R.id.iv_date_pick);
        datePickerButton.setOnClickListener(this);
        timePickerButton = rootView.findViewById(R.id.iv_time_pick);
        timePickerButton.setOnClickListener(this);
        addButton = rootView.findViewById(R.id.b_add_date_note);
        addButton.setOnClickListener(this);
        cancelButton = rootView.findViewById(R.id.b_cancel_date_note);
        cancelButton.setOnClickListener(this);
        pickedDate = rootView.findViewById(R.id.tv_picked_date);
        pickedTime = rootView.findViewById(R.id.tv_picked_time);
        model = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
        return rootView;
    }

    public void openTimePicker() {
        DialogFragment fragment = new TimePickerFragment();
        fragment.setTargetFragment(this, REQUEST_TIME);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    public void openDatePicker() {
        DialogFragment fragment = new DatePickerFragment();
        fragment.setTargetFragment(this, REQUEST_DATE);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    public void addNote() {
        model.insert(new DateNoteEntity(description.toString(), localTime, localDate));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_time_pick:
                openTimePicker();
                break;
            case R.id.iv_date_pick:
                openDatePicker();
                break;
            case R.id.b_add_date_note:
                addNote();
                getActivity().onBackPressed();
                break;
            case R.id.b_cancel_date_note:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TIME:
                    int[] weight = data.getIntArrayExtra("time");
                    localTime = LocalTime.of(weight[0], weight[1]);
                    pickedTime.setText(localTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
                    break;
                case REQUEST_DATE:
                    int[] date = data.getIntArrayExtra("date");
                    localDate = LocalDate.of(date[0], date[1], date[2]);
                    pickedDate.setText(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                    break;
            }
        }
    }
}
