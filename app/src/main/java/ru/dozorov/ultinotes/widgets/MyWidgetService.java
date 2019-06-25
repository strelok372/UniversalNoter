package ru.dozorov.ultinotes.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.fragments.AddNoteFragment;
import ru.dozorov.ultinotes.room.NoteDatabase;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.room.entities.NoteEntity;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class MyWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

    class MyRemoteViewsFactory implements RemoteViewsFactory {
        private int widgetId;
        private Context context;
        List<? extends NoteEntity> p;
        NoteDatabase database;
        int mode = 0;

        public MyRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            database = NoteDatabase.getInstance(context);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (p != null)
                return p.size();
            else return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(p != null) {
                switch (mode) {
                    case 0:
                        p = database.noteDao().getDateNotes().getValue();
                        break;
                    case 1:
                        p = database.noteDao().getUsualNotes().getValue();
                        break;
                    case 2:
                        p = database.noteDao().getActualToDo().getValue();
                        break;
                }
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
                rv.setTextViewText(R.id.tv_widget_item, p.get(position).getDescription());
                return rv;
            }
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
