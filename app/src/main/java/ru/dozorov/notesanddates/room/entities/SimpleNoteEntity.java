package ru.dozorov.notesanddates.room.entities;

import org.threeten.bp.OffsetDateTime;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(tableName = "notes")
public class SimpleNoteEntity implements NoteEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public OffsetDateTime updated;
    public String title;
    public String text;

    public SimpleNoteEntity(OffsetDateTime updated, String title, String text) {
        this.updated = updated;
        this.title = title;
        this.text = text;
    }

    @Override
    public String className() {
        return "SimpleNoteEntity";
    }
//Color?
}
