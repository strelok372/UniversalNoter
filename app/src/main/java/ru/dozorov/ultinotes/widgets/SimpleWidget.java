package ru.dozorov.ultinotes.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.lifecycle.ViewModelProviders;

import ru.dozorov.ultinotes.MainActivity;
import ru.dozorov.ultinotes.R;

public class SimpleWidget extends AppWidgetProvider {
    public static final String tag = "OOOOOOOOOOOOOOOO";
    public static final String ACTION_REFRESH = "refreshMyWidget";


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (ACTION_REFRESH.equals(intent.getAction())) {
            final int wid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            appWidgetManager.notifyAppWidgetViewDataChanged(wid, R.id.lv_widget);
            Log.i(tag, "On receive has done here.");
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i : appWidgetIds) {
            Intent changeButtonIntent = new Intent(context, SimpleWidget.class);
            changeButtonIntent.setAction(ACTION_REFRESH);
            changeButtonIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, i);
            PendingIntent pChangeButtonIntent = PendingIntent.getBroadcast(context, 5, changeButtonIntent, 0);

            Intent addNoteButtonIntent = new Intent(context, MainActivity.class);
            changeButtonIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, i);
            PendingIntent pAddButtonIntent = PendingIntent.getActivity(context, 4, addNoteButtonIntent, 0);

            Intent serviceIntent = new Intent(context, MyWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, i);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setRemoteAdapter(R.id.lv_widget, serviceIntent);
            remoteViews.setOnClickPendingIntent(R.id.b_change_widget, pChangeButtonIntent);
            remoteViews.setEmptyView(R.id.lv_widget, R.id.tv_widget_empty_view);

            appWidgetManager.updateAppWidget(i, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(i, R.id.lv_widget);
            Log.i(tag, "Updated widget with id: " + i);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
