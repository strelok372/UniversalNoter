package ru.dozorov.ultinotes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.adapters.RVSimpleNotesAdapter;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class SimpleNotesFragment extends Fragment {
    private NoteViewModel model;
    FragmentManager fragmentManager;
    RVSimpleNotesAdapter adapter;
    RVSimpleNotesAdapter.OnSimpleItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.simple_notes, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_simple_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listener = (RVSimpleNotesAdapter.OnSimpleItemClickListener) getActivity();
        model = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);

        adapter = new RVSimpleNotesAdapter(rootView.getContext());
        recyclerView.setAdapter(adapter);

        model.getUNotes().observe(this, new Observer<List<SimpleNoteEntity>>() {
            @Override
            public void onChanged(List<SimpleNoteEntity> list) {
                adapter.setNotes(list);
            }
        });

        adapter.setOnItemClickListener(listener);
//        adapter.setOnItemClickListener(new RVSimpleNotesAdapter.OnSimpleItemClickListener() {
//            @Override
//            public void onItemClick(SimpleNoteEntity entity) {
//                AddNoteFragment addNoteFragment = new AddNoteFragment().setEditMode(entity);
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
////                        .add(R.id.ll_simple_notes, addNoteFragment)
//                        .replace(R.id.ll_simple_notes, addNoteFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
        return rootView;
    }
//
//    public void translateListener(RVSimpleNotesAdapter.OnSimpleItemClickListener listener){
//        adapter.setOnItemClickListener(listener);
//    }
}
