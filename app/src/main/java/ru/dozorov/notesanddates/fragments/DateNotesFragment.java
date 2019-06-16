package ru.dozorov.notesanddates.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.adapters.RVDateNotesAdapter;
import ru.dozorov.notesanddates.room.entities.DateNoteEntity;
import ru.dozorov.notesanddates.room.entities.SimpleNoteEntity;
import ru.dozorov.notesanddates.viewmodel.NoteViewModel;

public class DateNotesFragment extends Fragment {
    private NoteViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.date_notes, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_date_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final RVDateNotesAdapter adapter = new RVDateNotesAdapter(rootView.getContext());
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

    protected void deleteNote(DateNoteEntity note){
        model.delete(note);
    }
}
