package ru.dozorov.ultinotes.room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.room.entities.NoteEntity;
import ru.dozorov.ultinotes.room.entities.ToDoEntity;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;

@Dao
public abstract class NoteDao {

    @Insert
    public abstract void insert(DateNoteEntity note);
    @Insert
    public abstract void insert(SimpleNoteEntity note);
    @Insert
    public abstract void insert(ToDoEntity note);

    @Delete
    public abstract void delete(DateNoteEntity note);
    @Delete
    public abstract void delete(SimpleNoteEntity note);
    @Delete
    public abstract void delete(ToDoEntity note);

    @Update
    public abstract void update(DateNoteEntity note);
    @Update
    public abstract void update(SimpleNoteEntity note);
    @Update
    public abstract void update(ToDoEntity note);

    @Transaction
    public void insert(NoteEntity noteEntity){
        switch (noteEntity.className()){
            case "DateNoteEntity":
                insert((DateNoteEntity)noteEntity);
                break;
            case "SimpleNoteEntity":
                insert((SimpleNoteEntity)noteEntity);
                break;
            case "ToDoEntity":
                insert((ToDoEntity)noteEntity);
                break;
        }
    }

    @Transaction
    public void delete(NoteEntity noteEntity){
        switch (noteEntity.className()){
            case "DateNoteEntity":
                delete((DateNoteEntity)noteEntity);
                break;
            case "SimpleNoteEntity":
                delete((SimpleNoteEntity)noteEntity);
                break;
            case "ToDoEntity":
                delete((ToDoEntity)noteEntity);
                break;
        }
    }

    @Transaction
    public void update(NoteEntity noteEntity){
        switch (noteEntity.className()){
            case "DateNoteEntity":
                update((DateNoteEntity)noteEntity);
                break;
            case "SimpleNoteEntity":
                update((SimpleNoteEntity)noteEntity);
                break;
            case "ToDoEntity":
                update((ToDoEntity)noteEntity);
                break;
        }
    }


    @Query("SELECT * FROM date_notes ORDER BY date, time DESC")
    public abstract LiveData<List<DateNoteEntity>> getDateNotes();

    @Query("SELECT * FROM notes ORDER BY updated DESC")
    public abstract LiveData<List<SimpleNoteEntity>> getUsualNotes();

    @Query("SELECT * FROM to_do WHERE actual = 1 ORDER BY position DESC")
    public abstract LiveData<List<ToDoEntity>> getActualToDo();

    @Query("SELECT * FROM to_do WHERE actual = 0  ORDER BY position DESC")
    public abstract LiveData<List<ToDoEntity>> getNotActualToDo();

    @RawQuery
    public abstract int checkpoint(SupportSQLiteQuery supportSQLiteQuery);
}
