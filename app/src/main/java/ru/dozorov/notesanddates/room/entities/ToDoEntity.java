package ru.dozorov.notesanddates.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(tableName = "to_do")
public class ToDoEntity implements NoteEntity{
    @PrimaryKey(autoGenerate = true)
    public int position;
    public String text;
    public int actual;

    public ToDoEntity(String text, int actual) {
        this.text = text;
        this.actual = actual;
    }


    @Override
    public String className() {
        return "ToDoEntity";
    }
}
