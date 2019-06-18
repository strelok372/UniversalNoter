package ru.dozorov.ultinotes.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.adapters.RVDateNotesAdapter;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class DateNotesFragment extends Fragment {
    private NoteViewModel model;
    KeyboardClosing keyboardClosing;
    RVDateNotesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.date_notes, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_date_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RVDateNotesAdapter(rootView.getContext());

        keyboardClosing = new KeyboardClosing() {
            @Override
            public void close() {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        };

        adapter.setKeyboardClosing(keyboardClosing);

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
        List<DateNoteEntity> entityList = adapter.getEntityList();
        for (DateNoteEntity s : entityList){
            Log.i("EWLRWELKRLKWE", "UPDATING!1");
            model.update(s);
        }
    }

    public interface KeyboardClosing{
        public void close();
    }

    protected void deleteNote(DateNoteEntity note){
        model.delete(note);
    }
}
