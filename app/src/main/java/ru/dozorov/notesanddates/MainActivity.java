package ru.dozorov.notesanddates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.dozorov.notesanddates.adapters.ViewPageAdapter;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ViewPager vPager;
    PagerAdapter pAdapter;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pAdapter = new ViewPageAdapter(getSupportFragmentManager(), 1); //???
        vPager = findViewById(R.id.viewpager);
        vPager.setAdapter(pAdapter);


    }
}
