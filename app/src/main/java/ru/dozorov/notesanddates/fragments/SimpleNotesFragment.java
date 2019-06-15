package ru.dozorov.notesanddates.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.dozorov.notesanddates.R;
import ru.dozorov.notesanddates.adapters.RVSimpleNotesAdapter;

public class SimpleNotesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.simple_notes, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_simple_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final RVSimpleNotesAdapter adapter = new RVSimpleNotesAdapter(rootView.getContext());
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
