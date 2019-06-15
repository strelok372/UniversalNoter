package ru.dozorov.notesanddates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.adapters.RVSimpleNotesAdapter;
import ru.dozorov.notesanddates.adapters.RVToDoListAdapter;

public class ToDoListFragment extends Fragment {
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
        return rootView;
    }
}
