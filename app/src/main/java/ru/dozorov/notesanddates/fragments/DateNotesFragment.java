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
import ru.dozorov.notesanddates.adapters.RVDateNotesAdapter;

public class DateNotesFragment extends Fragment {
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
        return rootView;
    }
}
