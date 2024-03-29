package ru.dozorov.ultinotes.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import lombok.Getter;
import ru.dozorov.ultinotes.repository.NoteRepository;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.room.entities.NoteEntity;
import ru.dozorov.ultinotes.room.entities.ToDoEntity;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repo;
    @Getter
    private LiveData<List<DateNoteEntity>> dateNotes;
    @Getter
    private LiveData<List<SimpleNoteEntity>> uNotes;
    @Getter
    private LiveData<List<ToDoEntity>> toDoNotes;


    public NoteViewModel(@NonNull Application application) {
        super(application);
        repo = new NoteRepository(application); //или гетконтекст
        dateNotes = repo.getDateNotes();
        uNotes = repo.getUNotes();
        toDoNotes = repo.getActualToDoNotes();
    }

    public void insert(NoteEntity note){
        repo.insert(note);
    }

    public void update(NoteEntity note){
        repo.update(note);
    }

    public void delete(NoteEntity note){
        repo.delete(note);
    }


}
