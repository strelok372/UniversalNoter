package ru.dozorov.ultinotes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.adapters.RVToDoListAdapter;
import ru.dozorov.ultinotes.room.entities.ToDoEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class ToDoListFragment extends Fragment {
    private NoteViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.to_do_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_to_do);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final RVToDoListAdapter adapter = new RVToDoListAdapter(rootView.getContext());
        recyclerView.setAdapter(adapter);
        model = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
        model.getToDoNotes().observe(this, new Observer<List<ToDoEntity>>() {
            @Override
            public void onChanged(List<ToDoEntity> list) {
                adapter.setNotes(list);
            }
        });
        return rootView;
    }
}
