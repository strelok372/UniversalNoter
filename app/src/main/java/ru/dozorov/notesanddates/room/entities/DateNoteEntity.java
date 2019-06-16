package ru.dozorov.notesanddates.room.entities;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(tableName = "date_notes")
public class DateNoteEntity implements NoteEntity{
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public String description;
    public LocalTime time;
    public LocalDate date;

    public DateNoteEntity(String description, LocalTime time, LocalDate date) {
        this.description = description;
        this.time = time;
        this.date = date;
    }


    @Override
    public String className() {
        return "DateNoteEntity";
    }
}
