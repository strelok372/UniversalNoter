package ru.dozorov.ultinotes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.room.NoteDatabase;

import static android.os.Environment.getExternalStorageDirectory;

public class FileManagerFragment extends Fragment implements View.OnClickListener {
    List<String> items;
    List<String> path;
    List<String> history;
    String root;
    ArrayAdapter<String> p;
    TextView showPath;
    ListView lwe;
    public final static String RETURN_DATA = "dataret";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.file_manager, container, false);

        lwe = v.findViewById(R.id.lw_file_manager);

        showPath = v.findViewById(R.id.tv_show_path);
        showPath.setText(root);

        v.findViewById(R.id.b_filemanager_ok).setOnClickListener(this);
        v.findViewById(R.id.b_filemanager_cancel).setOnClickListener(this);

        history = new ArrayList<>();
        history.add(root);

        NoteDatabase.getInstance(getContext());

        root = getExternalStorageDirectory().getPath();
        getDir(root);

        p = new ArrayAdapter<String>(getContext(), R.layout.file_manager_item, items);

        lwe.setAdapter(p);

        lwe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (!history.get(history.size() - 1).equals(root)) {
                        getDir(history.get(history.size() - 1));
                        history.remove(history.size() - 1);
                    } else return;
                } else {
                    File f = new File(path.get(position));
                    if (f.isDirectory()) {
                        if (f.canRead()) {
                            history.add(showPath.getText().toString());
                            getDir(f.getPath());
                        }
                    } else {
                        if (getTargetRequestCode() == LoginFragment.RESTORE_CODE) {
                            String choosedFile = history.get(history.size() - 1) + items.get(position);
                            showPath.setText(choosedFile);
                            view.setBackgroundColor(Color.parseColor("#e3f3ff"));
                        }
                    }
                }
            }
        });

        return v;
    }

    boolean isStorageReadable() {
        String s = Environment.getExternalStorageState();
        return s.equals(Environment.MEDIA_MOUNTED_READ_ONLY) || s.equals(Environment.MEDIA_MOUNTED);
    }

    boolean isStorageWritable() {
        String s = Environment.getExternalStorageState();
        return s.equals(Environment.MEDIA_MOUNTED);
    }


    void getDir(String dirpath) {
        items = new ArrayList<>();
        path = new ArrayList<>();

        File f = new File(dirpath);
        File[] files = f.listFiles();

//        if (dirpath.equals(root)) {
//            items.add("../");
//        }


        items.add("/");
        path.add("/");

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() == o2.isDirectory()) {
                    return o1.getName().compareTo(o2.getName());
                } else if (o1.isDirectory())
                    return -1;
                else
                    return 1;
            }
        });

        showPath.setText(dirpath);

        for (File t : files) {
            path.add(t.getPath());
            if (t.isDirectory()) items.add(t.getName() + "/");
            else items.add(t.getName());
        }
        p = new ArrayAdapter<String>(getContext(), R.layout.file_manager_item, items);
//        p.notifyDataSetChanged();
        lwe.setAdapter(p);

//        return dirpath;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_filemanager_cancel:
                getChildFragmentManager().popBackStack();
                break;

            case R.id.b_filemanager_ok:
                Intent intent = new Intent();
                intent.putExtra(RETURN_DATA, showPath.getText().toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                break;
        }
    }
}
