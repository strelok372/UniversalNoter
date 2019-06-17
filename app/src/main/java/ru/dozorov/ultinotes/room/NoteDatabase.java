package ru.dozorov.ultinotes.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;
import ru.dozorov.ultinotes.room.entities.ToDoEntity;

@Database(entities = {DateNoteEntity.class, SimpleNoteEntity.class, ToDoEntity.class}, version = 1)
@TypeConverters(NoteTypeConverters.class)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase database;
    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration().build();
            return database;
        }
            return database;
    }
}
