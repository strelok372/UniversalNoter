package ru.dozorov.ultinotes.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ru.dozorov.ultinotes.R;

public class DatePickerFragment extends DialogFragment {
    private DatePicker datePicker;

    @Override
    public void onStart() {
        super.onStart();
        datePicker = getDialog().findViewById(R.id.dp_picker);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //???
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.date_picker_dialog, null))
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        int[] s = new int[] {datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()};
                        intent.putExtra("date", s);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePickerFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
