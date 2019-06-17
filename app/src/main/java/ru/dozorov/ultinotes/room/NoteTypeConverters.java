package ru.dozorov.ultinotes.room;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import androidx.room.TypeConverter;

public class NoteTypeConverters {
//    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//    DateTimeFormatter dateFormatterer = DateTimeFormatter.ISO_DATE;
//    DateTimeFormatter timeFormatterer = DateTimeFormatter.ISO_TIME;

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value){
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(value, OffsetDateTime.FROM);
    }

    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime dateTime){
        return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @TypeConverter
    public static LocalDate toLocalDate(String value){
        return DateTimeFormatter.ISO_DATE.parse(value, LocalDate.FROM);
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate date){
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    @TypeConverter
    public static LocalTime toLocalTime(String value){
        if (value != null)
            return DateTimeFormatter.ISO_TIME.parse(value, LocalTime.FROM);
        return null;
    }

    @TypeConverter
    public static String fromLocalTime(LocalTime time){
        if (time != null)
            return time.format(DateTimeFormatter.ISO_TIME);
        return null;
    }
}
