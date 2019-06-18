package ru.dozorov.ultinotes.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

public class UltiNoteBackupAgent extends BackupAgentHelper {
    static final String PREFS = "user_preferences";
    static final String PREFS_KEY = "prefs";
        static final String DB = "databases/note_database";
    static final String DB_KEY = "database";
    final Object syncObj = new Object();

    @Override
    public void onCreate() {
        Log.i("MYLOGGGGGGGGGGG: ", "JA RODILSA!!!");
        SharedPreferencesBackupHelper sharedP = new SharedPreferencesBackupHelper(this, PREFS, getPackageName()+"_preferences");
        FileBackupHelper fileBackupHelper = new FileBackupHelper(this, "../databases/note_database");
        addHelper(PREFS_KEY, sharedP);
        addHelper(DB_KEY, fileBackupHelper);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        Log.i("MYLOGGGGGGGGGGG: ", "Backuped");
        synchronized (syncObj) {
            super.onBackup(oldState, data, newState);
        }
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        Log.i("MYLOGGGGGGGGGGG: ", "RESTORED!!1");
        synchronized (syncObj) {
            super.onRestore(data, appVersionCode, newState);
        }
    }

    public static void requestBackup(Context context) {
        BackupManager bm = new BackupManager(context);
        bm.dataChanged();
    }
}
