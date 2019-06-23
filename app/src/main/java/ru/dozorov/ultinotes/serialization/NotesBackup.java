package ru.dozorov.ultinotes.serialization;

import java.io.Serializable;
import java.util.List;

import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.room.entities.NoteEntity;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;
import ru.dozorov.ultinotes.room.entities.ToDoEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class NotesBackup implements Serializable {
    List<SimpleNoteEntity> noteEntities;
    List<ToDoEntity> toDoEntities;
    List<DateNoteEntity> dateNoteEntities;



    public void fillObject(NoteViewModel nvm){
        dateNoteEntities = nvm.getDateNotes().getValue();
        toDoEntities = nvm.getToDoNotes().getValue();
        noteEntities = nvm.getUNotes().getValue();
    }

    public void fillDB(NoteViewModel nvm){
        for (SimpleNoteEntity s : noteEntities)
            nvm.insert(s);
        for (ToDoEntity t : toDoEntities)
            nvm.insert(t);
        for (DateNoteEntity d : dateNoteEntities)
            nvm.insert(d);
    }
}
