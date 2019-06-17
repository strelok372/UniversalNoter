package ru.dozorov.ultinotes.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ru.dozorov.ultinotes.R;

public class TimePickerFragment extends DialogFragment {
    TimePicker timePicker;

    @Override
    public void onStart() {
        super.onStart();
        timePicker = getDialog().findViewById(R.id.tp_dialog);
        timePicker.setIs24HourView(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //???
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.time_picker_dialog, null))
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        int[] s;
                        if (Build.VERSION.SDK_INT >= 23 ) {
                            s = new int[]{timePicker.getHour(), timePicker.getMinute()};
                        }
                        else {
                            s = new int[]{timePicker.getCurrentHour(), timePicker.getCurrentMinute()};
                        }
                        intent.putExtra("time", s);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimePickerFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
