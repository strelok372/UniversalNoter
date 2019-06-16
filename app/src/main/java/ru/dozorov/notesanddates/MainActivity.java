package ru.dozorov.notesanddates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.dozorov.notesanddates.adapters.ViewPageAdapter;
import ru.dozorov.notesanddates.fragments.AddDateNoteFragment;
import ru.dozorov.notesanddates.fragments.AddNoteFragment;
import ru.dozorov.notesanddates.fragments.DateNotesFragment;
import ru.dozorov.notesanddates.fragments.ToDoListFragment;
import ru.dozorov.notesanddates.room.entities.ToDoEntity;
import ru.dozorov.notesanddates.viewmodel.NoteViewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolbar);
        barLayout = findViewById(R.id.app_bar_layout);
        vPager = findViewById(R.id.viewpager);
        fab = findViewById(R.id.fab_main);
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
        tabLayout.setupWithViewPager(vPager);

        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fab.setImageResource(drawRes.get(position));
                if (position == 2)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                    addToDo.setVisibility(View.VISIBLE);
//                else addToDo.setVisibility(View.INVISIBLE);
                closeNoteAddFragment();
//                fab.
//                vPager.getAdapter()
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_resource, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
//        Toast.makeText(this, String.valueOf(vPager.getCurrentItem()), Toast.LENGTH_SHORT).show();
//        getSupportFragmentManager().findFragmentById(R.layout.note_add_layout);
        switch (view.getId()){
            case R.id.fab_main:
                switch (vPager.getCurrentItem()){
                    case 0:
                        addNoteFragment(new AddDateNoteFragment());
                        break;
                    case 1:
                        addNoteFragment(new AddNoteFragment());
                        break;
                    case 2:
                        if (!addToDo.getText().toString().isEmpty()){
                            noteViewModel.insert(new ToDoEntity(addToDo.getText().toString(), 1));
                            addToDo.getText().clear();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fab.isOrWillBeHidden())
            fab.setVisibility(View.VISIBLE);
    }

    public void addNoteFragment(Fragment fragment){
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            fab.setVisibility(View.INVISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_add_note, fragment, "addNewNote");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void closeNoteAddFragment(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            onBackPressed();
            if (fab.isOrWillBeHidden())
                fab.setVisibility(View.VISIBLE);
        };
    }
}
