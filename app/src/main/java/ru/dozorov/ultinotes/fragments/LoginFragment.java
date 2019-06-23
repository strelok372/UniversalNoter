package ru.dozorov.ultinotes.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ru.dozorov.ultinotes.R;
import ru.dozorov.ultinotes.serialization.NotesBackup;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class LoginFragment extends Fragment implements View.OnClickListener {
    public static final int BACKUP_CODE = 201;
    public static final int RESTORE_CODE = 202;
    Button signInButton;
    Button backupButton;
    ImageView userIcon;
    TextView lastSaved;
    TextView userLogin;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    String lastBU;
    private static final int RC_SIGN_IN = 22;
    int Mode = 1;
    OnLoginFragmentListener listener;

    public LoginFragment(String lastBU) {
        this.lastBU = lastBU;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        backupButton = view.findViewById(R.id.b_backup_now);
        backupButton.setOnClickListener(this);

        view.findViewById(R.id.b_backup_to_file).setOnClickListener(this);
        view.findViewById(R.id.b_restore_from_file).setOnClickListener(this);
        view.findViewById(R.id.b_close_login).setOnClickListener(this);

        signInButton = view.findViewById(R.id.b_signin_signout);
        signInButton.setOnClickListener(this);

        lastSaved = view.findViewById(R.id.tv_last_backup);
        userLogin = view.findViewById(R.id.tv_user_login);
        userIcon = view.findViewById(R.id.iv_account_icon);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        listener = (OnLoginFragmentListener) getActivity();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account != null) {
            lastSaved.setText("Last backup: " + lastBU);
            updateUi();
        } else lastSaved.setText("There is no backup");
        return view;
    }

    void updateUi() {
        backupButton.setClickable(true);

        Drawable d = backupButton.getBackground();
        d.setTint(ContextCompat.getColor(getContext(), R.color.deepBlue));
        backupButton.setBackground(d);
//        backupButton.setBackgroundColor(getResources().getColor(R.color.deepBlue));

        signInButton.setText("Sign out ");

        userLogin.setText(account.getDisplayName());

        Mode = 2;
        if (account.getPhotoUrl() != null) {
            d = null;
            try {
                d = new AsyncTask<Void, Void, Drawable>() {
                    @Override
                    protected Drawable doInBackground(Void[] objects) {
                        String url = account.getPhotoUrl().toString();
                        InputStream is = null;
                        try {
                            is = (InputStream) new URL(url).getContent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return Drawable.createFromStream(is, "src name");
                    }
                }.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userIcon.setImageDrawable(d);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void exportDB(String path) {
        try {
            NotesBackup nb = new NotesBackup();
            nb.fillObject(ViewModelProviders.of(getActivity()).get(NoteViewModel.class));
            File exported = new File(path + "/" + "UltiNotes(" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ").bckp");
            if (exported.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(exported);
                new ObjectOutputStream(fos).writeObject(nb);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void importDB(String filePath) {
        if (filePath.endsWith(".bckp")) {
            File db = new File(filePath);
            try {
                FileInputStream fis = new FileInputStream(db);
                ObjectInputStream ois = new ObjectInputStream(fis);
                NotesBackup nb;
                nb = (NotesBackup) ois.readObject();
                nb.fillDB(ViewModelProviders.of(getActivity()).get(NoteViewModel.class));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Please, choose the correct file", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
            case RESTORE_CODE:
                importDB(data.getStringExtra(FileManagerFragment.RETURN_DATA));
                break;
            case BACKUP_CODE:
                exportDB(data.getStringExtra(FileManagerFragment.RETURN_DATA));
                break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            listener.getLoginInfo(account);
            updateUi();
//            Toast.makeText(getContext(),"Auth success for " + account.getEmail(), Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            Log.w("MY_LOG_HERE: ", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getContext(), "Auth failed with code " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.b_restore_from_file:
                if (checkFilePermission())
                    openFileExplorer(RESTORE_CODE);
                break;

            case R.id.b_backup_to_file:
                if (checkFilePermission())
                    openFileExplorer(RESTORE_CODE);
                break;
            case R.id.b_signin_signout:
                switch (Mode) {
                    case 1:
                        signIn();
                        break;
                    case 2:
//                        mGoogleSignInClient.signOut();
                        break;
                }
                break;

            case R.id.b_backup_now:
                lastBU = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                lastSaved.setText("Last backup: " + lastBU);
                listener.doBackupNow();
                break;

            case R.id.b_close_login:
                getActivity().onBackPressed();
                break;
        }
    }

    boolean checkFilePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    openFileExplorer();
                }
                Toast.makeText(getContext(), "File storage permission denied", Toast.LENGTH_SHORT);
        }
    }

    void openFileExplorer(int code) {
        FileManagerFragment fmf = new FileManagerFragment();
        fmf.setTargetFragment(this, code);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.cv_login, fmf)
//                .add(R.id.cv_login , new FileManagerFragment())
                .addToBackStack(null)
                .commit();
    }

    public interface OnLoginFragmentListener {
        public void getLoginInfo(GoogleSignInAccount account);

        public void doBackupNow();
    }

}
