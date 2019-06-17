package ru.dozorov.ultinotes.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import lombok.Getter;
import ru.dozorov.ultinotes.room.NoteDao;
import ru.dozorov.ultinotes.room.NoteDatabase;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.room.entities.NoteEntity;
import ru.dozorov.ultinotes.room.entities.ToDoEntity;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;

public class NoteRepository { //можно сделать синглтоном || методы через asynctask || что будут возвращать не гет-методы?
    NoteDao dao;
    @Getter
    LiveData<List<DateNoteEntity>> dateNotes;
    @Getter
    LiveData<List<SimpleNoteEntity>> uNotes;
    @Getter
    LiveData<List<ToDoEntity>> actualToDoNotes;

    public NoteRepository(Application application) {
        NoteDatabase db = NoteDatabase.getInstance(application.getApplicationContext());
        dao = db.noteDao();
        dateNotes = dao.getDateNotes();
        uNotes = dao.getUsualNotes();
        actualToDoNotes = dao.getActualToDo();
//        uNotes.getValue().get(0).get
    }

    public void insert(NoteEntity note){
        new methodAsyncTask(dao, 1).execute(note);
    }

    public void delete(NoteEntity note){
        new methodAsyncTask(dao, 3).execute(note);
    }

    public void update(NoteEntity note){
        new methodAsyncTask(dao, 2).execute(note);
    }

    private static class methodAsyncTask extends AsyncTask<NoteEntity, Void, Void>{
        private NoteDao dao;
        private int number;

        public methodAsyncTask(NoteDao dao, int number) {
            this.dao = dao;
            this.number = number;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            switch (number){
                case 1:
                    dao.insert(noteEntities[0]);
                    break;
                case 2:
                    dao.update(noteEntities[0]);
                    break;
                case 3:
                    dao.delete(noteEntities[0]);
                    break;
            }
            return null;
        }
    }
}
