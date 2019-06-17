package ru.dozorov.notesanddates.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.adapters.RVSimpleNotesAdapter;
import ru.dozorov.notesanddates.room.entities.SimpleNoteEntity;
import ru.dozorov.notesanddates.viewmodel.NoteViewModel;

public class SimpleNotesFragment extends Fragment {
    private NoteViewModel model;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.simple_notes, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_simple_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        model = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);

        final RVSimpleNotesAdapter adapter = new RVSimpleNotesAdapter(rootView.getContext());
        recyclerView.setAdapter(adapter);

        model.getUNotes().observe(this, new Observer<List<SimpleNoteEntity>>() {
            @Override
            public void onChanged(List<SimpleNoteEntity> list) {
                adapter.setNotes(list);
            }
        });

        adapter.setOnItemClickListener(new RVSimpleNotesAdapter.OnSimpleItemClickListener() {
            @Override
            public void onItemClick(SimpleNoteEntity entity) {
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.ll_simple_notes, new AddNoteFragment().setEditMode(entity))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}
