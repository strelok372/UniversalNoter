package ru.dozorov.ultinotes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import ru.dozorov.ultinotes.adapters.RVSimpleNotesAdapter;
import ru.dozorov.ultinotes.adapters.ViewPageAdapter;
import ru.dozorov.ultinotes.backup.UltiNoteBackupAgent;
import ru.dozorov.ultinotes.fragments.AddNoteFragment;
import ru.dozorov.ultinotes.fragments.LoginFragment;
import ru.dozorov.ultinotes.room.NoteDao;
import ru.dozorov.ultinotes.room.NoteDatabase;
import ru.dozorov.ultinotes.room.entities.DateNoteEntity;
import ru.dozorov.ultinotes.room.entities.SimpleNoteEntity;
import ru.dozorov.ultinotes.room.entities.ToDoEntity;
import ru.dozorov.ultinotes.transition.VerticalFlipTransition;
import ru.dozorov.ultinotes.viewmodel.NoteViewModel;

public class MainActivity extends AppCompatActivity implements RVSimpleNotesAdapter.OnSimpleItemClickListener, LoginFragment.OnLoginFragmentListener, KeyboardInterface {
    private ViewPager vPager;
    private TabLayout tabLayout;
    private AppBarLayout barLayout;
    PagerAdapter pAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private List<Integer> drawRes;
    private NoteViewModel noteViewModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private EditText addToDo;
    String lastBackuped;
    GoogleSignInAccount account;
    NoteDao noteDao;
    MenuItem backupButton;
    InputMethodManager imm;
    String tempResult;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UltiNoteBackupAgent.requestBackup(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolbar);
        barLayout = findViewById(R.id.app_bar_layout);
        vPager = findViewById(R.id.viewpager);
        fab = findViewById(R.id.fab_main);


        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (checkFilePermission()){
                    recognizeSpeech(vPager.getCurrentItem());
                    return true;
                }
                return false;
            }
        });
        NestedScrollView bottomSheet = findViewById(R.id.nsv_main_activity);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        addToDo = findViewById(R.id.et_add_to_do_main);
        drawRes = new ArrayList<>();
        drawRes.add(R.drawable.ic_date_note_add);
        drawRes.add(R.drawable.ic_simple_note_add);
        drawRes.add(R.drawable.ic_to_do_note_add);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        setSupportActionBar(toolbar);
        pAdapter = new ViewPageAdapter(getSupportFragmentManager(), 1); //???
        vPager.setAdapter(pAdapter);
        vPager.setPageTransformer(false, new VerticalFlipTransition());
        tabLayout.setupWithViewPager(vPager);

        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fab.setImageResource(drawRes.get(position));
                if (position == 2)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                closeNoteAddFragment();
            }

            @Override
            public void onPageSelected(int position) {
                hideK();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        account = GoogleSignIn.getLastSignedInAccount(this);

        noteDao = NoteDatabase.getInstance(this).noteDao();

        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_resource, menu);
        backupButton = menu.findItem(R.id.i_backup);
        if (account != null) backupButton.setIcon(R.drawable.ic_cloud);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.i_backup:
//                fab.setVisibility(View.INVISIBLE);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.coord_layout, new LoginFragment(lastBackuped), "addNewNote")
//                        .addToBackStack(null)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .commit();
                addNoteFragment(new LoginFragment(lastBackuped));
                break;
            case R.id.i_settings:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideK() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void onClick(View view) {
//        Toast.makeText(this, String.valueOf(vPager.getCurrentItem()), Toast.LENGTH_SHORT).show();
//        getSupportFragmentManager().findFragmentById(R.layout.note_add_layout);
        switch (view.getId()) {
            case R.id.fab_main:
                switch (vPager.getCurrentItem()) {
                    case 0:
                        noteViewModel.insert(new DateNoteEntity("", null, LocalDate.now()));
//                        addNoteFragment(new AddDateNoteFragment());
                        break;
                    case 1:
                        addNoteFragment(new AddNoteFragment());
                        break;
                    case 2:
                        if (!addToDo.getText().toString().isEmpty()) {
                            noteViewModel.insert(new ToDoEntity(addToDo.getText().toString(), 1));
                            addToDo.getText().clear();
                            hideKeyboard();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fab.isOrWillBeHidden())
            fab.setVisibility(View.VISIBLE);
    }

    public void addNoteFragment(Fragment fragment) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            fab.setVisibility(View.INVISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_add_note, fragment, "addNewNote");
//            fragmentTransaction.replace(R.id.fl_add_note, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }

    public void closeNoteAddFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            onBackPressed();
        }
    }

    public void backUp() {
        if (account != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    noteDao.checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
                }
            }).start();
            Log.i("MYLOGGGGGGGGGGG: ", "Query is completed?");
            UltiNoteBackupAgent.requestBackup(this);
            lastBackuped = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onItemClick(SimpleNoteEntity entity) {
        AddNoteFragment addNoteFragment = new AddNoteFragment().setEditMode(entity);
        addNoteFragment(addNoteFragment);
    }


    @Override
    public void getLoginInfo(GoogleSignInAccount account) {
        this.account = account;
        if (account == null) lastBackuped = null;
    }

    @Override
    public void doBackupNow() {
        if (account == null) Log.i("SDFSLKDFLKSD", "Account is null");
        backUp();
        backupButton.setIcon(R.drawable.ic_cloud);
    }

    @Override
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void showKeyboard(final View view) {
        if (view == null) {
            View mv = getCurrentFocus();
            imm.showSoftInput(mv, InputMethodManager.SHOW_IMPLICIT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (view.requestFocus())
                        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        }
    }

    void recognizeSpeech(int a) {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            startActivityForResult(intent, a);
        } catch (ActivityNotFoundException e) {
            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox"));
            startActivity(browse);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> p = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            switch (requestCode){
                case 0:
                    noteViewModel.insert(new DateNoteEntity(p.get(0), null, LocalDate.now()));
                    break;
                case 1:
                    AddNoteFragment anf = new AddNoteFragment();
                    anf.createWithText(p.get(0));
                    addNoteFragment(anf);
                    break;
                case 2:
                    noteViewModel.insert(new ToDoEntity(p.get(0), 1));
                    break;
            }

//            ArrayList<String> p = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            Log.i("WIOEIWJQEIOQWJIOE", String.valueOf(p.get(0)));
//            tempResult = p.get(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    boolean checkFilePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 2);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT);
//                    recognizeSpeech();
                }
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT);
        }
    }
}
