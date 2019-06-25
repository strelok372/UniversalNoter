package ru.dozorov.ultinotes.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(tableName = "to_do")
public class ToDoEntity implements NoteEntity, Serializable {
    @PrimaryKey(autoGenerate = true)
    public transient int position;
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

    @Override
    public String getDescription() {
        return text;
    }
}
