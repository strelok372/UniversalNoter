package ru.dozorov.ultinotes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import ru.dozorov.ultinotes.KeyboardInterface;
import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.adapters.RVDateNotesAdapter;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class DateNotesFragment extends Fragment implements RVDateNotesAdapter.DateTimePickerDialogCall {
    private NoteViewModel model;
    RVDateNotesAdapter adapter;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_DATE = 2;
    DateNoteEntity temp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.date_notes, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_date_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RVDateNotesAdapter(rootView.getContext());

        adapter.setDateTimePicker(this);

        adapter.setKeyboardInterface((KeyboardInterface) getActivity());

        recyclerView.setAdapter(adapter);
        model = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
        model.getDateNotes().observe(this, new Observer<List<DateNoteEntity>>() {
            @Override
            public void onChanged(List<DateNoteEntity> list) {
                adapter.setNotes(list);
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter.getEntityList() != null){
            List<DateNoteEntity> entityList = adapter.getEntityList();
            for (DateNoteEntity s : entityList){
                Log.i("EWLRWELKRLKWE", "UPDATING!1");
                model.update(s);
            }
        }
    }

    protected void deleteNote(DateNoteEntity note){
        model.delete(note);
    }

    @Override
    public void setTime(DateNoteEntity entity) {
        temp = entity;
        DialogFragment fragment = new TimePickerFragment();
        fragment.setTargetFragment(this, REQUEST_TIME);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    @Override
    public void setDate(DateNoteEntity entity) {
        temp = entity;
        DialogFragment fragment = new DatePickerFragment();
        fragment.setTargetFragment(this, REQUEST_DATE);
        fragment.show(getFragmentManager(), fragment.getClass().getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TIME:
                    int[] weight = data.getIntArrayExtra("time");
                    temp.setTime(LocalTime.of(weight[0], weight[1]));
                    break;
                case REQUEST_DATE:
                    int[] date = data.getIntArrayExtra("date");
                    temp.setDate(LocalDate.of(date[0], date[1], date[2]));
                    break;
            }
        }
    }


}
